package com.sparta.scheduledevelop.domain.comment.entity;

import com.sparta.scheduledevelop.domain.schedule.entity.Schedule;
import com.sparta.scheduledevelop.domain.common.entity.Timestamped;
import com.sparta.scheduledevelop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User commentCreator;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Comment(String text, User commentCreator, Schedule schedule) {
        this.text = text;
        this.commentCreator = commentCreator;
        this.schedule = schedule;

        commentCreator.getCommentList().add(this);
        schedule.getCommentList().add(this);
    }
}
