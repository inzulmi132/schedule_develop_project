package com.sparta.scheduledevelop.entity;

import com.sparta.scheduledevelop.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "schedules")
public class Schedule extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String todo;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    /*
    @ManyToMany
    @JoinTable(name = "total",
    joinColumns = @JoinColumn(name = "schedule_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList = new ArrayList<>();
     */

    public Schedule(String title, String todo) {
        this.title = title;
        this.todo = todo;
    }

    public Schedule(ScheduleRequestDto dto, User creator) {
        this.title = dto.getTitle();
        this.todo = dto.getTodo();
        this.creator = creator;
    }

    public void update(String title, String todo) {
        this.title = title;
        this.todo = todo;
    }
}
