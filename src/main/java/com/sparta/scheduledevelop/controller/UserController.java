package com.sparta.scheduledevelop.controller;

import com.sparta.scheduledevelop.dto.UserRequestDto;
import com.sparta.scheduledevelop.dto.UserResponseDto;
import com.sparta.scheduledevelop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String signup(@Valid UserRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        return userService.signup(dto);
    }

    @PostMapping("/login")
    public String login(UserRequestDto dto, HttpServletResponse response) {
        return userService.login(dto, response);
    }

    @GetMapping("/users")
    public List<UserResponseDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping("/edit")
    public String updateUser(HttpServletRequest request, @Valid UserRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        return userService.updateUser(request, dto);
    }

    @DeleteMapping("/quit")
    public String deleteUser(HttpServletRequest request) {
        return userService.deleteUser(request);
    }

    public boolean validationCheck(List<FieldError> fieldErrors) {
        if(fieldErrors.isEmpty()) return false;
        for(FieldError fieldError : fieldErrors)
            log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
        return true;
    }
}
