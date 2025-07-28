package com.tus.traunreut.repository;

import com.tus.traunreut.ScheduledTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledTaskRepository extends JpaRepository<ScheduledTask, Long> {
    List<ScheduledTask> findByExecutionTimeAfter(LocalDateTime now);

    @Query("SELECT t FROM ScheduledTask t WHERE t.executionTime >= :pastLimit")
    List<ScheduledTask> findAllPendingTasks(@Param("pastLimit") LocalDateTime pastLimit);
}
