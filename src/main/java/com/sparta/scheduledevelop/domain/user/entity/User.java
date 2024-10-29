package com.sparta.scheduledevelop.domain.user.entity;

import com.sparta.scheduledevelop.domain.comment.entity.Comment;
import com.sparta.scheduledevelop.domain.common.entity.Timestamped;
import com.sparta.scheduledevelop.domain.schedule.entity.Schedule;
import com.sparta.scheduledevelop.domain.schedule.entity.ScheduleAuthor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "scheduleCreator", cascade = CascadeType.REMOVE)
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "author")
    private List<ScheduleAuthor> authList;

    @OneToMany(mappedBy = "commentCreator")
    private List<Comment> commentList;

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
