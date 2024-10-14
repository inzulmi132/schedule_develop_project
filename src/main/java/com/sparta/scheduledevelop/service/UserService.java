package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.config.PasswordEncoder;
import com.sparta.scheduledevelop.dto.UserRequestDto;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.repository.UserRepository;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public String login(UserRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        return email;
    }

    public List<User> findAllUsers() {
        return userRepository.findAllByOrderByModifiedAtDesc();
    }

    public String updateUser(User user, UserRequestDto dto) {
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return "Update Success";
    }

    public String deleteUser(User user) {
        userRepository.delete(user);
        return "Delete Success";
    }
}
