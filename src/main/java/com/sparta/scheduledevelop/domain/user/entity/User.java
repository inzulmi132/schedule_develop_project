package com.sparta.scheduledevelop.domain.user.entity;

import com.sparta.scheduledevelop.domain.comment.entity.Comment;
import com.sparta.scheduledevelop.domain.common.entity.Timestamped;
import com.sparta.scheduledevelop.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "scheduleCreator")
    private List<Schedule> scheduleList = new ArrayList<>();

    @ManyToMany(mappedBy = "authorList")
    private List<Schedule> authList = new ArrayList<>();

    @OneToMany(mappedBy = "commentCreator")
    private List<Comment> commentList = new ArrayList<>();

    public User(String email, String name, String password, UserRoleEnum role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public void update(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
