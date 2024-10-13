package com.sparta.scheduledevelop.controller;

import com.sparta.scheduledevelop.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    final ScheduleService scheduleService;
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto dto) {
        return scheduleService.createSchedule(dto);
    }

    @GetMapping("")
    public List<ScheduleResponseDto> getAllSchedules(@RequestParam(required = false) String username, @RequestParam(required = false) String modifiedAt) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(modifiedAt);
        if(username != null && modifiedAt != null) return scheduleService.findSchedulesByUsernameAndModifiedAt(username, date);
        if(modifiedAt != null) return scheduleService.findSchedulesByModifiedAt(date);
        if(username != null) return scheduleService.findSchedulesByUsername(username);
        return scheduleService.findSchedules();
    }

    @GetMapping("/{id}")
    public ScheduleResponseDto getSchedule(@PathVariable Long id) {
        return scheduleService.findScheduleById(id);
    }

    @PutMapping("/{id}")
    public ScheduleResponseDto updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto dto) {
        return scheduleService.updateSchedule(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
    }
}
