package com.sparta.scheduledevelop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String todo;
}