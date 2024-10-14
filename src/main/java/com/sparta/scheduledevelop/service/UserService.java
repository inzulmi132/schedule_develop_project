package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.config.PasswordEncoder;
import com.sparta.scheduledevelop.dto.UserRequestDto;
import com.sparta.scheduledevelop.dto.UserResponseDto;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.jwt.JwtUtil;
import com.sparta.scheduledevelop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Controller
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String signup(UserRequestDto dto) {
        String email = dto.getEmail();
        String username = dto.getUsername();
        String password = passwordEncoder.encode(dto.getPassword());

        Optional<User> checkUser = userRepository.findByEmail(email);
        if(checkUser.isPresent()) return "중복된 Email 입니다.";

        User user = new User(email, username, password);
        userRepository.save(user);
        return "Signup Success";
    }

    public String login(UserRequestDto dto, HttpServletResponse response) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) return "등록된 Email이 없습니다.";

        if(!passwordEncoder.matches(password, user.getPassword())) return "비밀번호가 일치하지 않습니다.";

        String token = jwtUtil.createToken(user.getEmail());
        jwtUtil.addJwtToCookie(token, response);
        return "Login Success";
    }

    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAllByOrderByModifiedAtDesc().stream().map(UserResponseDto::new).toList();
    }

    @Transactional
    public String updateUser(HttpServletRequest request, UserRequestDto dto) {
        User user = (User) request.getAttribute("user");
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return "Update Success";
    }

    public String deleteUser(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        userRepository.delete(user);
        return "Delete Success";
    }
}
