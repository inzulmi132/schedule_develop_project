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
import com.sparta.scheduledevelop.exception.CustomErrorCode;
import com.sparta.scheduledevelop.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .orElseThrow(() -> new CustomException(CustomErrorCode.SCHEDULE_NOT_FOUND));
        if(!Objects.equals(user.getId(), schedule.getScheduleCreator().getId()))
            throw new CustomException(CustomErrorCode.INVALID_ADD_AUTHOR);
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));

        ScheduleAuthor scheduleAuthor = new ScheduleAuthor(schedule, author);
        scheduleAuthorRepository.save(scheduleAuthor);
    }

    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllByOrderByModifiedAtDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    public ScheduleResponseDto findScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.SCHEDULE_NOT_FOUND));
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
                .orElseThrow(() -> new CustomException(CustomErrorCode.SCHEDULE_NOT_FOUND));
        if(isNotAuthorized(user, role, schedule)) {
            throw new CustomException(CustomErrorCode.INVALID_UPDATE);
        }
        schedule.update(requestDto.getTitle(), requestDto.getTodo());
        return new ScheduleResponseDto(scheduleRepository.saveAndFlush(schedule));
    }

    @Transactional
    public void deleteSchedule(User user, UserRoleEnum role, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.SCHEDULE_NOT_FOUND));
        if(isNotAuthorized(user, role, schedule)) {
            throw new CustomException(CustomErrorCode.INVALID_DELETE);
        }
        scheduleAuthorRepository.deleteAll(schedule.getAuthorList());
        scheduleRepository.deleteById(scheduleId);
    }

    // 수정 및 삭제 시 권한을 확인하는 메서드
    public boolean isNotAuthorized(User user, UserRoleEnum role, Schedule schedule) {
        if(role == UserRoleEnum.ADMIN) {
            return false;
        }

        User creator = schedule.getScheduleCreator();
        if(Objects.equals(creator.getId(), user.getId())) {
            return false;
        }

        List<User> authorList = schedule.getAuthorList().stream().map(ScheduleAuthor::getAuthor).toList();
        return !authorList.contains(user);
    }
}
