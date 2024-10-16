package com.sparta.scheduledevelop.controller;

import com.sparta.scheduledevelop.client.WeatherClient;
import com.sparta.scheduledevelop.client.WeatherResponse;
import com.sparta.scheduledevelop.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.service.ScheduleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

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

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));
        String weather = weatherClient.getWeather().stream()
                .filter(weatherResponse -> Objects.equals(weatherResponse.getDate(), today))
                .map(WeatherResponse::getWeather)
                .findFirst()
                .orElse(null);
        if(weather == null) return "Weather Not Found";

        return scheduleService.createSchedule(user, dto, weather);
    }

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
        String role = (String) request.getAttribute("role");
        if(!Objects.equals(role, "ADMIN")) {
            response.setStatus(403);
            return "관리자만 일정을 수정할 수 있습니다.";
        }

        User user = (User) request.getAttribute("user");
        return scheduleService.updateSchedule(user, role, scheduleId, dto);
    }

    @DeleteMapping("/{scheduleId}")
    public String deleteSchedule(HttpServletRequest request, @PathVariable Long scheduleId, HttpServletResponse response) {
        String role = (String) request.getAttribute("role");
        if(!Objects.equals(role, "ADMIN")) {
            response.setStatus(403);
            return "관리자만 일정을 삭제할 수 있습니다.";
        }

        User user = (User) request.getAttribute("user");
        return scheduleService.deleteSchedule(user, role, scheduleId);
    }

    public boolean validationCheck(List<FieldError> fieldErrors) {
        if(fieldErrors.isEmpty()) return false;
        for(FieldError fieldError : fieldErrors)
            log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
        return true;
    }
}
