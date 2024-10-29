package com.sparta.scheduledevelop.domain.schedule.repository;

import com.sparta.scheduledevelop.domain.schedule.entity.ScheduleAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleAuthorRepository extends JpaRepository<ScheduleAuthor, Long> {
}
