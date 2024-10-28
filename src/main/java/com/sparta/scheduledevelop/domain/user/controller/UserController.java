package com.sparta.scheduledevelop.domain.user.controller;

import com.sparta.scheduledevelop.domain.user.dto.UserRequestDto;
import com.sparta.scheduledevelop.domain.user.dto.UserResponseDto;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.jwt.JwtUtil;
import com.sparta.scheduledevelop.domain.user.service.UserService;
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

    // filter 의 인가 여부 구분을 위해서 GetMapping
    @GetMapping("/signup")
    public String signup(@Valid UserRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        return userService.signup(dto);
    }

    // filter 의 인가 여부 구분을 위해서 GetMapping
    @GetMapping("/login")
    public String login(UserRequestDto dto, HttpServletResponse response) {
        User user = userService.login(dto);
        if(user == null) {
            response.setStatus(401);
            return "Login Failed";
        }
        // 로그인한 유저의 정보로 토큰 생성
        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
        jwtUtil.addJwtToCookie(token, response);
        return "Login Success";
    }

    @GetMapping
    public List<UserResponseDto> findAllUsers() {
        return userService.findAllUsers().stream().map(UserResponseDto::new).toList();
    }

    @PutMapping
    public String updateUser(HttpServletRequest request, @Valid UserRequestDto dto, BindingResult bindingResult) {
        if(validationCheck(bindingResult.getFieldErrors())) return "Validation Exception";
        User user = (User) request.getAttribute("user");
        return userService.updateUser(user, dto);
    }

    @DeleteMapping
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
