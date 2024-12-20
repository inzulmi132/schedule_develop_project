package com.sparta.scheduledevelop.domain.schedule.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    @Size(min = 1, max = 20)
    private String title;
    @Size(min = 1, max = 500)
    private String todo;
}
