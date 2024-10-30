package com.sparta.scheduledevelop.domain.schedule.entity;

import com.sparta.scheduledevelop.domain.comment.entity.Comment;
import com.sparta.scheduledevelop.domain.common.entity.Timestamped;
import com.sparta.scheduledevelop.domain.schedule.dto.ScheduleRequestDto;
import com.sparta.scheduledevelop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
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
    @Column(nullable = false)
    private String weather;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User scheduleCreator;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<ScheduleAuthor> authorList;

    public Schedule(User scheduleCreator, ScheduleRequestDto dto, String weather) {
        this.scheduleCreator = scheduleCreator;
        this.title = dto.getTitle();
        this.todo = dto.getTodo();
        this.weather = weather;
    }

    public void update(String title, String todo) {
        this.title = title;
        this.todo = todo;
    }
}
