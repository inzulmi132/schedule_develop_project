package com.sparta.scheduledevelop.domain.schedule.entity;

import com.sparta.scheduledevelop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedule_author")
public class ScheduleAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public ScheduleAuthor(Schedule schedule, User author) {
        this.schedule = schedule;
        this.author = author;
    }
}
