package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.entity.Schedule;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.repository.ScheduleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public String createSchedule(HttpServletRequest request, ScheduleRequestDto dto) {
        User creator = (User) request.getAttribute("user");
        Schedule schedule = new Schedule(dto, creator);
        scheduleRepository.save(schedule);
        return "Schedule created";
    }

    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleRepository.findAllByOrderByModifiedAtDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    public ScheduleResponseDto findScheduleById(Long scheduleId) {
        return new ScheduleResponseDto(findById(scheduleId));
    }

    public String updateSchedule(HttpServletRequest request, Long scheduleId, ScheduleRequestDto dto) {
        Schedule schedule = findById(scheduleId);
        User creator = schedule.getCreator();
        User user = (User) request.getAttribute("user");

        if(!Objects.equals(creator.getId(), user.getId())) return "You are not allowed to update this schedule";
        schedule.setTitle(dto.getTitle());
        schedule.setTodo(dto.getTodo());
        scheduleRepository.save(schedule);
        return "Schedule updated";
    }

    public String deleteSchedule(HttpServletRequest request, Long scheduleId) {
        Schedule schedule = findById(scheduleId);
        User creator = schedule.getCreator();
        User user = (User) request.getAttribute("user");

        if(!Objects.equals(creator.getId(), user.getId())) return "You are not allowed to delete this schedule";
        scheduleRepository.deleteById(scheduleId);
        return "Schedule deleted";
    }

    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }
}
