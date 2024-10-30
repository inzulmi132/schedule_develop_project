package com.sparta.scheduledevelop.domain.schedule.controller;

import com.sparta.scheduledevelop.domain.common.annotation.LoginRole;
import com.sparta.scheduledevelop.domain.common.annotation.LoginUser;
import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleResponseDto;
import com.sparta.scheduledevelop.domain.schedule.service.ScheduleService;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.domain.user.entity.UserRoleEnum;
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
            @LoginUser User user,
            @Valid ScheduleRequestDto requestDto
    ) {
        ScheduleResponseDto responseDto = scheduleService.createSchedule(user, requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDto);
    }

    // 일정 담장 유저 배치
    @PostMapping("/{scheduleId}/author/{authorId}")
    public ResponseEntity<String> addScheduleAuthor(
            @LoginUser User user,
            @PathVariable Long scheduleId,
            @PathVariable Long authorId
    ) {
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
        return scheduleService.findAllSchedulesByPage(page-1, size);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @LoginUser User user,
            @LoginRole UserRoleEnum role,
            @PathVariable Long scheduleId,
            @Valid ScheduleRequestDto dto
    ) {
        ScheduleResponseDto responseDto = scheduleService.updateSchedule(user, role, scheduleId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(
            @LoginUser User user,
            @LoginRole UserRoleEnum role,
            @PathVariable Long scheduleId
    ) {
        scheduleService.deleteSchedule(user, role, scheduleId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Schedule deleted successfully");
    }
}
