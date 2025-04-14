package com.gracjan_d.taskmanager.repository;

import com.gracjan_d.taskmanager.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskJpaRepository extends JpaRepository<Task, Integer> {
    List<Task> findByTaskType(TaskType taskType);
    List<Task> findByTaskTypeAndStatus(TaskType taskType, Status status);
    List<Task> findByStatus(Status status);
    List<Task> findByPriority(Priority priority);
    List<Task> findByTaskTypeAndPriority(TaskType taskType, Priority priority);
    List<Task> findByTaskTypeAndDifficulty(TaskType taskType, Difficulty difficulty);
    @Query("SELECT COUNT(*) FROM Task")
    int countTasks();
    int countByStatus(Status status);
}
