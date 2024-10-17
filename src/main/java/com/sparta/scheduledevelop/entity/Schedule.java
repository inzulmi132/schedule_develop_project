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
    @Column(nullable = false)
    private String weather;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User scheduleCreator;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "userSchedule",
    joinColumns = @JoinColumn(name = "schedule_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> authorList = new ArrayList<>();

    public Schedule(User scheduleCreator, ScheduleRequestDto dto, String weather) {
        this.scheduleCreator = scheduleCreator;
        this.title = dto.getTitle();
        this.todo = dto.getTodo();
        this.weather = weather;

        // 생성할 때 작성자의 일정 리스트에 추가
        scheduleCreator.getScheduleList().add(this);
    }
}
