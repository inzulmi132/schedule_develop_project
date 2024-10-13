package com.sparta.scheduledevelop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
public class UserRequestDto {
    @Email
    @UniqueElements
    private String email;
    @Min(3)
    private String username;
    @Min(6)
    private String password;
}
