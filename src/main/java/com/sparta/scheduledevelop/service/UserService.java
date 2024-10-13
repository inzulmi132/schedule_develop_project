package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.dto.UserRequestDto;
import com.sparta.scheduledevelop.dto.UserResponseDto;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto dto) {
        User user = new User(dto);
        return new UserResponseDto(userRepository.save(user));
    }

    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAllByOrderByModifiedAtDesc().stream().map(UserResponseDto::new).toList();
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.update(dto);
        return new UserResponseDto(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
