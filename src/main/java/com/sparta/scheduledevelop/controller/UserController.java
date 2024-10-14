package com.sparta.scheduledevelop.controller;

import com.sparta.scheduledevelop.dto.UserRequestDto;
import com.sparta.scheduledevelop.dto.UserResponseDto;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.jwt.JwtUtil;
import com.sparta.scheduledevelop.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "userController")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public String signup(@Valid UserRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        return userService.signup(dto);
    }

    @PostMapping("/login")
    public String login(UserRequestDto dto, HttpServletResponse response) {
        String token = jwtUtil.createToken(userService.login(dto));
        jwtUtil.addJwtToCookie(token, response);
        return "Login Success";
    }

    @GetMapping
    public List<UserResponseDto> findAllUsers() {
        return userService.findAllUsers().stream().map(UserResponseDto::new).toList();
    }

    @PutMapping("/edit")
    public String updateUser(HttpServletRequest request, @Valid UserRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        User user = (User) request.getAttribute("user");
        return userService.updateUser(user, dto);
    }

    @DeleteMapping("/delete")
    public String deleteUser(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return userService.deleteUser(user);
    }

    public boolean validationCheck(List<FieldError> fieldErrors) {
        if(fieldErrors.isEmpty()) return false;
        for(FieldError fieldError : fieldErrors)
            log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
        return true;
    }
}
