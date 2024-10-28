package com.sparta.scheduledevelop.domain.schedule.service;

import com.sparta.scheduledevelop.client.WeatherClient;
import com.sparta.scheduledevelop.client.WeatherResponse;
import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.domain.schedule.entity.Schedule;
import com.sparta.scheduledevelop.domain.schedule.repository.ScheduleRepository;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.domain.user.entity.UserRoleEnum;
import com.sparta.scheduledevelop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public String createSchedule(User creator, ScheduleRequestDto dto) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));
        String weather = weatherClient.getWeather()
                .stream()
                .filter(weatherResponse -> Objects.equals(weatherResponse.getDate(), today))
                .map(WeatherResponse::getWeather)
                .findFirst()
                .orElse(null);
        Schedule schedule = new Schedule(creator, dto, weather);
        scheduleRepository.save(schedule);
        return "Schedule created";
    }

    // 일정의 작성자가 담당 유저 배치
    @Transactional
    public String addScheduleAuthor(User user, Long scheduleId, Long authorId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if(schedule == null) return "Schedule not found";
        if(!Objects.equals(user.getEmail(), schedule.getScheduleCreator().getEmail()))
            return "You are not allowed to add author this schedule";
        User author = userRepository.findById(authorId).orElse(null);
        if(author == null) return "Author not found";

        schedule.getAuthorList().add(author);
        author.getAuthList().add(schedule);

        return "Author added";
    }

    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAllByOrderByModifiedAtDesc();
    }

    public Schedule findScheduleById(Long scheduleId) {
        return findById(scheduleId);
    }

    public Page<Schedule> findAllSchedulesByPage(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return scheduleRepository.findAll(pageable);
    }

    @Transactional
    public String updateSchedule(User user, UserRoleEnum role, Long scheduleId, ScheduleRequestDto dto) {
        Schedule schedule = findById(scheduleId);
        if(!isAuthorized(user, role, schedule))
            return "You are not allowed to update this schedule";

        schedule.update(dto.getTitle(), dto.getTodo());
        return "Schedule updated";
    }

    @Transactional
    public String deleteSchedule(User user, UserRoleEnum role, Long scheduleId) {
        Schedule schedule = findById(scheduleId);
        if(!isAuthorized(user, role, schedule))
            return "You are not allowed to delete this schedule";

        User creator = schedule.getScheduleCreator();
        creator.getScheduleList().remove(schedule);
        for(User author : schedule.getAuthorList()) author.getScheduleList().remove(schedule);
        scheduleRepository.deleteById(scheduleId);
        return "Schedule deleted";
    }

    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    // 수정 및 삭제 시 권한을 확인하는 메서드
    public boolean isAuthorized(User user, UserRoleEnum role, Schedule schedule) {
        User creator = schedule.getScheduleCreator();
        List<User> authorList = schedule.getAuthorList();
        return Objects.equals(creator.getEmail(), user.getEmail()) || authorList.contains(user) || role == UserRoleEnum.ADMIN;
    }
}
