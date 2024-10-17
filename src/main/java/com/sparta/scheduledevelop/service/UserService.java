package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.config.PasswordEncoder;
import com.sparta.scheduledevelop.dto.UserRequestDto;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.entity.UserRoleEnum;
import com.sparta.scheduledevelop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String signup(UserRequestDto dto) {
        String email = dto.getEmail();
        String username = dto.getUsername();
        String password = passwordEncoder.encode(dto.getPassword());

        User checkUser = userRepository.findByEmail(email).orElse(null);
        if(checkUser != null) return "중복된 Email 입니다.";

        UserRoleEnum role = UserRoleEnum.USER;
        if(dto.isAdmin()) {
            if(!Objects.equals(dto.getAdminToken(), ADMIN_TOKEN)) return "관리자 암호가 틀렸습니다.";
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(email, username, password, role);
        userRepository.save(user);
        return "Signup Success";
    }

    public User login(UserRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        User user = userRepository.findByEmail(email).orElse(null);
        // 이메일 또는 비밀번호가 틀리면 null 반환
        if(user == null) return null;
        if(!passwordEncoder.matches(password, user.getPassword())) return null;

        return user;
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
