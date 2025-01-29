package org.example.repository;

import org.example.model.tasks.TaskComment;
import org.springframework.data.repository.CrudRepository;

public interface ITaskCommentRepository extends CrudRepository<TaskComment, Integer> {
}
