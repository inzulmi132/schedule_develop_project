package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.entity.Schedule;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public String createSchedule(User creator, ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto, creator);
        scheduleRepository.save(schedule);
        return "Schedule created";
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

    public String updateSchedule(User user, Long scheduleId, ScheduleRequestDto dto) {
        Schedule schedule = findById(scheduleId);
        if(isAuthorized(user, schedule)) return "You are not allowed to update this schedule";

        schedule.setTitle(dto.getTitle());
        schedule.setTodo(dto.getTodo());
        scheduleRepository.save(schedule);
        return "Schedule updated";
    }

    public String deleteSchedule(User user, Long scheduleId) {
        Schedule schedule = findById(scheduleId);
        if(isAuthorized(user, schedule)) return "You are not allowed to delete this schedule";
        scheduleRepository.deleteById(scheduleId);
        return "Schedule deleted";
    }

    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public boolean isAuthorized(User user, Schedule schedule) {
        User creator = schedule.getScheduleCreator();
        List<User> authorList = schedule.getAuthorList();
        return authorList.contains(user) || creator.getId().equals(user.getId());
    }
}
