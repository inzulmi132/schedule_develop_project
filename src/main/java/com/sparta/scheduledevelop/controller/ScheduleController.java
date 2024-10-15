package com.sparta.scheduledevelop.controller;

import com.sparta.scheduledevelop.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
        User user = (User) request.getAttribute("user");
        return scheduleService.createSchedule(user, dto);
    }

    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleService.findAllSchedules().stream().map(ScheduleResponseDto::new).toList();
    }

    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto findSchedule(@PathVariable Long scheduleId) {
        return new ScheduleResponseDto(scheduleService.findScheduleById(scheduleId));
    }

    @GetMapping("/paging")
    public Page<ScheduleResponseDto> findAllSchedulesByPage(int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return scheduleService.findAllSchedulesByPage(page-1, size).map(ScheduleResponseDto::new);
    }

    @PutMapping("/{scheduleId}/edit")
    public String updateSchedule(HttpServletRequest request, @PathVariable Long scheduleId, @Valid ScheduleRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        User user = (User) request.getAttribute("user");
        return scheduleService.updateSchedule(user, scheduleId, dto);
    }

    @DeleteMapping("/{scheduleId}/delete")
    public String deleteSchedule(HttpServletRequest request, @PathVariable Long scheduleId) {
        User user = (User) request.getAttribute("user");
        return scheduleService.deleteSchedule(user, scheduleId);
    }

    public boolean validationCheck(List<FieldError> fieldErrors) {
        if(fieldErrors.isEmpty()) return false;
        for(FieldError fieldError : fieldErrors)
            log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
        return true;
    }
}
