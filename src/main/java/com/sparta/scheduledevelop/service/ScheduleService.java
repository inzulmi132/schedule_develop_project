package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.entity.Schedule;
import com.sparta.scheduledevelop.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {
    final private ScheduleRepository scheduleRepository;
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto);
        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    public List<ScheduleResponseDto> findSchedules() {
        return scheduleRepository.findAllByOrderByModifiedAtDesc().stream().map(ScheduleResponseDto::new).toList();
    }

    public List<ScheduleResponseDto> findSchedulesByUsername(String username) {
        return scheduleRepository.findAllByUsernameOrderByModifiedAtDesc(username).stream().map(ScheduleResponseDto::new).toList();
    }

    public List<ScheduleResponseDto> findSchedulesByModifiedAt(Date modifiedAt) {
        return scheduleRepository.findAllByModifiedAtOrderByModifiedAtDesc(modifiedAt).stream().map(ScheduleResponseDto::new).toList();
    }

    public List<ScheduleResponseDto> findSchedulesByUsernameAndModifiedAt(String username, Date modifiedAt) {
        return scheduleRepository.findAllByUsernameAndModifiedAtOrderByModifiedAtDesc(username, modifiedAt).stream().map(ScheduleResponseDto::new).toList();
    }

    public ScheduleResponseDto findScheduleById(Long id) {
        return new ScheduleResponseDto(findById(id));
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto dto) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.update(dto);
        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
    }
}
