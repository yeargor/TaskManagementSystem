package org.example.repository;

import org.example.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ITaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
//    @Query("SELECT t FROM Task t WHERE t.author.id = :userId OR :userId IN (SELECT e.id FROM t.executors e)")
//    Page<Task> findTasksByUser(@Param("userId") String userId, Pageable pageable);
}