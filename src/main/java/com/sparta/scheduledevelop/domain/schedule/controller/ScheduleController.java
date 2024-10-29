package com.sparta.scheduledevelop.domain.schedule.controller;

import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.domain.schedule.service.ScheduleService;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.domain.user.entity.UserRoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "scheduleController")
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            HttpServletRequest request,
            @Valid ScheduleRequestDto requestDto
    ) {
        User user = (User) request.getAttribute("user");
        ScheduleResponseDto responseDto = scheduleService.createSchedule(user, requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // 일정 담장 유저 배치
    @PostMapping("/{scheduleId}/author/{authorId}")
    public ResponseEntity<String> addScheduleAuthor(
            HttpServletRequest request,
            @PathVariable Long scheduleId,
            @PathVariable Long authorId
    ) {
        User user = (User) request.getAttribute("user");
        scheduleService.addScheduleAuthor(user, scheduleId, authorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Schedule Author added successfully");
    }

    @GetMapping
    public List<ScheduleResponseDto> findAllSchedules() {
        return scheduleService.findAllSchedules();
    }

    @GetMapping("/{scheduleId}")
    public ScheduleResponseDto findSchedule(@PathVariable Long scheduleId) {
        return scheduleService.findScheduleById(scheduleId);
    }

    @GetMapping("/paging")
    public Page<ScheduleResponseDto> findAllSchedulesByPage(
            int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return scheduleService.findAllSchedulesByPage(page-1, size).map(ScheduleResponseDto::new);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            HttpServletRequest request,
            @PathVariable Long scheduleId,
            @Valid ScheduleRequestDto dto
    ) {
        User user = (User) request.getAttribute("user");
        UserRoleEnum role = (UserRoleEnum) request.getAttribute("role");
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(user, role, scheduleId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(
            HttpServletRequest request,
            @PathVariable Long scheduleId
    ) {
        User user = (User) request.getAttribute("user");
        UserRoleEnum role = (UserRoleEnum) request.getAttribute("role");
        scheduleService.deleteSchedule(user, role, scheduleId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Schedule deleted successfully");
    }
}
