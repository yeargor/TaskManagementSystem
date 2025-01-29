package org.example.repository.tasks;

import org.example.model.tasks.Task;
import org.springframework.data.repository.CrudRepository;

public interface ITaskRepository extends CrudRepository<Task, Integer> {
}