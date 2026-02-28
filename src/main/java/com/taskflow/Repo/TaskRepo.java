package com.taskflow.Repo;

import com.taskflow.Entity.Task;
import com.taskflow.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {
    Page<Task> findByUser(User user, Pageable pageable);

    List<Task> findByUser(User user);

    Page<Task> findByUserAndTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            User user, String title, String description, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.deadline BETWEEN :start AND :end")
    List<Task> findTasksWithDeadlineBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT t FROM Task t WHERE t.deadline < :now")
    List<Task> findTasksWithDeadlineBefore(@Param("now") LocalDateTime now);

    @Query("SELECT t FROM Task t WHERE t.user = :user " +
            "AND (:start IS NULL OR t.deadline >= :start) " +
            "AND (:end IS NULL OR t.deadline <= :end) " +
            "AND (:status IS NULL OR :status = '' OR LOWER(t.status) = LOWER(:status))")
    Page<Task> findTasksWithFilters(@Param("user") User user,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("status") String status,
            Pageable pageable);
}
