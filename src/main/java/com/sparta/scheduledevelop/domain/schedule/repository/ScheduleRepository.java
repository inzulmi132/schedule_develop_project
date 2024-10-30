package com.sparta.scheduledevelop.domain.schedule.repository;

import com.sparta.scheduledevelop.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByModifiedAtDesc();
    Page<Schedule> findAllByOrderByModifiedAtDesc(Pageable pageable);
}
