package com.sparta.scheduledevelop.domain.schedule.controller;

import com.sparta.scheduledevelop.client.WeatherClient;
import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.domain.user.entity.UserRoleEnum;
import com.sparta.scheduledevelop.domain.schedule.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final WeatherClient weatherClient;
    public ScheduleController(ScheduleService scheduleService, WeatherClient weatherClient) {
        this.scheduleService = scheduleService;
        this.weatherClient = weatherClient;
    }

    @PostMapping
    public String createSchedule(HttpServletRequest request, @Valid ScheduleRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        User user = (User) request.getAttribute("user");
        return scheduleService.createSchedule(user, dto);
    }

    // 일정 담장 유저 배치
    @PostMapping("/{scheduleId}/{authorId}")
    public String addScheduleAuthor(HttpServletRequest request, @PathVariable Long scheduleId, @PathVariable Long authorId) {
        User user = (User) request.getAttribute("user");
        return scheduleService.addScheduleAuthor(user, scheduleId, authorId);
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

    @PutMapping("/{scheduleId}")
    public String updateSchedule(HttpServletRequest request, @PathVariable Long scheduleId, @Valid ScheduleRequestDto dto, BindingResult bindingResult, HttpServletResponse response) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        User user = (User) request.getAttribute("user");
        // 일정 수정은 권한도 확인
        UserRoleEnum role = (UserRoleEnum) request.getAttribute("role");
        return scheduleService.updateSchedule(user, role, scheduleId, dto);
    }

    @DeleteMapping("/{scheduleId}")
    public String deleteSchedule(HttpServletRequest request, @PathVariable Long scheduleId, HttpServletResponse response) {
        User user = (User) request.getAttribute("user");
        // 일정 삭제는 권한도 확인
        UserRoleEnum role = (UserRoleEnum) request.getAttribute("role");
        return scheduleService.deleteSchedule(user, role, scheduleId);
    }

    public boolean validationCheck(List<FieldError> fieldErrors) {
        if(fieldErrors.isEmpty()) return false;
        for(FieldError fieldError : fieldErrors)
            log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
        return true;
    }
}
