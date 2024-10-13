package com.sparta.scheduledevelop.repository;

import com.sparta.scheduledevelop.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByOrderByModifiedAtDesc();
    List<Schedule> findAllByUsernameOrderByModifiedAtDesc(String username);
    List<Schedule> findAllByModifiedAtOrderByModifiedAtDesc(Date modifiedAt);
    List<Schedule> findAllByUsernameAndModifiedAtOrderByModifiedAtDesc(String username, Date modifiedAt);
}
