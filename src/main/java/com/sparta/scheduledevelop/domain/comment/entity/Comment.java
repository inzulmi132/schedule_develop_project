package com.sparta.scheduledevelop.domain.comment.entity;

import com.sparta.scheduledevelop.domain.schedule.entity.Schedule;
import com.sparta.scheduledevelop.domain.common.entity.Timestamped;
import com.sparta.scheduledevelop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User commentCreator;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Comment(String content, User commentCreator, Schedule schedule) {
        this.content = content;
        this.commentCreator = commentCreator;
        this.schedule = schedule;
    }

    public void update(String text) {
        this.content = text;
    }
}
