package com.sparta.scheduledevelop.dto;

import com.sparta.scheduledevelop.entity.Schedule;
import lombok.Getter;

import java.util.Date;

@Getter
public class ScheduleResponseDto {
    private final Long id;
    private final String username;
    private final String title;
    private final Date createdAt;
    private final Date modifiedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.username = schedule.getUsername();
        this.title = schedule.getTitle();
        this.createdAt = schedule.getCreatedAt();
        this.modifiedAt = schedule.getModifiedAt();
    }
}
