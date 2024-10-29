package com.sparta.scheduledevelop.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserUpdateRequestDto {
    @NotBlank
    private String name;
    @Size(min = 6, max = 20)
    private String password;
}
