package com.sparta.scheduledevelop.domain.schedule.repository;

import com.sparta.scheduledevelop.domain.schedule.entity.ScheduleAuthor;
import com.sparta.scheduledevelop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleAuthorRepository extends JpaRepository<ScheduleAuthor, Long> {
    List<ScheduleAuthor> findAllByAuthor(User author);
}
