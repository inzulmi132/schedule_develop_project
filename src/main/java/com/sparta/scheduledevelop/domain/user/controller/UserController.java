package com.sparta.scheduledevelop.domain.user.controller;

import com.sparta.scheduledevelop.domain.user.dto.LoginRequestDto;
import com.sparta.scheduledevelop.domain.user.dto.SignupRequestDto;
import com.sparta.scheduledevelop.domain.user.dto.UserResponseDto;
import com.sparta.scheduledevelop.domain.user.dto.UserUpdateRequestDto;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.domain.user.service.UserService;
import com.sparta.scheduledevelop.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "userController")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid SignupRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) throw new RuntimeException("Validation Exception");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.signup(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(LoginRequestDto dto, HttpServletResponse response) {
        String token = userService.login(dto);
        jwtUtil.addJwtToCookie(token, response);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Login Successful");
    }

    @GetMapping
    public List<UserResponseDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(HttpServletRequest request, @Valid UserUpdateRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) throw new RuntimeException("Validation Exception");
        User user = (User) request.getAttribute("user");
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userService.updateUser(user, dto));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        userService.deleteUser(user);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Delete Successful");
    }

    public boolean validationCheck(List<FieldError> fieldErrors) {
        if(fieldErrors.isEmpty()) return false;
        for(FieldError fieldError : fieldErrors)
            log.error("{} 필드 : {}", fieldError.getField(), fieldError.getDefaultMessage());
        return true;
    }
}
