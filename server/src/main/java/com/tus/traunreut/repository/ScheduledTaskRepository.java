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

    /**
     * Finds all ScheduledTask entities with the given matchId.
     *
     * @param matchId The match ID to search for.
     * @return A list of ScheduledTask entities that match the given matchId.
     */
    List<ScheduledTask> findAllByMatchId(String matchId);
}
