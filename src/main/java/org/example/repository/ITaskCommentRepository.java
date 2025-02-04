package org.example.repository;

import org.example.model.task.TaskComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ITaskCommentRepository extends JpaRepository<TaskComment, Integer> {
    Page<TaskComment> findByTaskId(Integer taskId, Pageable pageable);
}
