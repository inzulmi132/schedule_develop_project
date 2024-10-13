package com.sparta.scheduledevelop.entity;

import com.sparta.scheduledevelop.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long userId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 500)
    private String todo;

    public Schedule(ScheduleRequestDto dto) {
        this.userId = dto.getUserId();
        this.title = dto.getTitle();
        this.todo = dto.getTodo();
    }

    public void update(ScheduleRequestDto dto) {
        this.setTitle(dto.getTitle());
        this.setTodo(dto.getTodo());
    }
}
