package com.sparta.scheduledevelop.domain.user.service;

import com.sparta.scheduledevelop.config.PasswordEncoder;
import com.sparta.scheduledevelop.domain.user.dto.LoginRequestDto;
import com.sparta.scheduledevelop.domain.user.dto.SignupRequestDto;
import com.sparta.scheduledevelop.domain.user.dto.UserResponseDto;
import com.sparta.scheduledevelop.domain.user.dto.UserUpdateRequestDto;
import com.sparta.scheduledevelop.domain.user.entity.User;
import com.sparta.scheduledevelop.domain.user.entity.UserRoleEnum;
import com.sparta.scheduledevelop.domain.user.repository.UserRepository;
import com.sparta.scheduledevelop.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserResponseDto signup(SignupRequestDto dto) {
        String email = dto.getEmail();
        String username = dto.getName();
        String password = passwordEncoder.encode(dto.getPassword());

        Optional<User> checkUser = userRepository.findByEmail(email);
        if(checkUser.isPresent()) throw new RuntimeException("중복된 Email 입니다.");

        UserRoleEnum role = UserRoleEnum.USER;
        if(dto.isAdmin()) {
            if(!Objects.equals(dto.getAdminToken(), ADMIN_TOKEN)) throw new RuntimeException("관리자 암호가 틀렸습니다.");
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(email, username, password, role);
        return new UserResponseDto(userRepository.save(user));
    }

    public String login(LoginRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자 입니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호 입니다.");
        }

        return jwtUtil.createToken(email, user.getRole());
    }

    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAllByOrderByModifiedAtDesc().stream().map(UserResponseDto::new).toList();
    }

    @Transactional
    public UserResponseDto updateUser(User user, UserUpdateRequestDto dto) {
        String name = dto.getName();
        String password = passwordEncoder.encode(dto.getPassword());
        user.update(name, password);
        return new UserResponseDto(userRepository.saveAndFlush(user));
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
