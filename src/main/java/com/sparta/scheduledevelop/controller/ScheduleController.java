package com.sparta.scheduledevelop.controller;

import com.sparta.scheduledevelop.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "scheduleController")
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/write")
    public String createSchedule(HttpServletRequest request, @Valid ScheduleRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        return scheduleService.createSchedule(request, dto);
    }

    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleService.findAllSchedules();
    }

    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto getSchedule(@PathVariable Long scheduleId) {
        return scheduleService.findScheduleById(scheduleId);
    }

    @PutMapping("/{scheduleId}/edit")
    public String updateSchedule(HttpServletRequest request, @PathVariable Long scheduleId, @Valid ScheduleRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        return scheduleService.updateSchedule(request, scheduleId, dto);
    }

    @DeleteMapping("/{scheduleId}/delete")
    public String deleteSchedule(HttpServletRequest request, @PathVariable Long scheduleId) {
        return scheduleService.deleteSchedule(request, scheduleId);
    }

    public boolean validationCheck(List<FieldError> fieldErrors) {
        if(fieldErrors.isEmpty()) return false;
        for(FieldError fieldError : fieldErrors)
            log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
        return true;
    }
}
