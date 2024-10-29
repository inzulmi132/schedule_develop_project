package com.sparta.scheduledevelop.domain.schedule.service;

import com.sparta.scheduledevelop.client.WeatherService;
import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.domain.schedule.entity.Schedule;
import com.sparta.scheduledevelop.domain.schedule.entity.ScheduleAuthor;
import com.sparta.scheduledevelop.domain.schedule.repository.ScheduleAuthorRepository;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleAuthorRepository scheduleAuthorRepository;
    private final WeatherService weatherService;

    @Transactional
    public ScheduleResponseDto createSchedule(User creator, ScheduleRequestDto requestDto) {
        String weather = weatherService.getWeather();
        Schedule schedule = new Schedule(creator, requestDto, weather);
        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    // 일정의 작성자가 담당 유저 배치
    public void addScheduleAuthor(User user, Long scheduleId, Long authorId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("Schedule not found"));
        if(!Objects.equals(user.getId(), schedule.getScheduleCreator().getId()))
            throw new RuntimeException("You are not allowed to add author this schedule");
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NoSuchElementException("Author not found"));

        ScheduleAuthor scheduleAuthor = new ScheduleAuthor(schedule, author);
        scheduleAuthorRepository.save(scheduleAuthor);
    }

    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllByOrderByModifiedAtDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    public ScheduleResponseDto findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        return new ScheduleResponseDto(schedule);
    }

    public Page<Schedule> findAllSchedulesByPage(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        return scheduleRepository.findAll(pageable);
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(User user, UserRoleEnum role, Long scheduleId, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        if(!isAuthorized(user, role, schedule)) {
            throw new RuntimeException("You are not allowed to update this schedule");
        }
        schedule.update(requestDto.getTitle(), requestDto.getTodo());
        return new ScheduleResponseDto(scheduleRepository.saveAndFlush(schedule));
    }

    @Transactional
    public void deleteSchedule(User user, UserRoleEnum role, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        if(!isAuthorized(user, role, schedule)) {
            throw new RuntimeException("You are not allowed to delete this schedule");
        }
        scheduleAuthorRepository.deleteAll(schedule.getAuthorList());
        scheduleRepository.deleteById(scheduleId);
    }

    // 수정 및 삭제 시 권한을 확인하는 메서드
    public boolean isAuthorized(User user, UserRoleEnum role, Schedule schedule) {
        User creator = schedule.getScheduleCreator();
        List<User> authorList = schedule.getAuthorList().stream().map(ScheduleAuthor::getAuthor).toList();
        return Objects.equals(creator.getEmail(), user.getEmail()) || authorList.contains(user) || role == UserRoleEnum.ADMIN;
    }
}
