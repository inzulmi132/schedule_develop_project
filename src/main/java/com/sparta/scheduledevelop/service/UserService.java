package com.sparta.scheduledevelop.service;

import com.sparta.scheduledevelop.config.PasswordEncoder;
import com.sparta.scheduledevelop.dto.UserRequestDto;
import com.sparta.scheduledevelop.entity.User;
import com.sparta.scheduledevelop.entity.UserRoleEnum;
import com.sparta.scheduledevelop.repository.UserRepository;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
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

        Optional<User> checkUser = userRepository.findByEmail(email);
        if(checkUser.isPresent()) return "중복된 Email 입니다.";

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

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));
        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

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
