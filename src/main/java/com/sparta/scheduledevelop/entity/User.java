package com.sparta.scheduledevelop.entity;

import com.sparta.scheduledevelop.dto.UserRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotBlank
    private String username;
    @Column
    @NotBlank
    private String password;
    @Column
    @Email
    private String email;

    public User(UserRequestDto dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
    }

    public void update(UserRequestDto dto) {
        this.email = dto.getEmail();
    }
}
