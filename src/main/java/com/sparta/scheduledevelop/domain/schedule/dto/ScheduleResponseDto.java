package com.sparta.scheduledevelop.domain.schedule.dto;

import com.sparta.scheduledevelop.domain.schedule.entity.Schedule;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private final Long id;
    private final String title;
    private final String todo;
    private final String weather;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.todo = schedule.getTodo();
        this.weather = schedule.getWeather();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
