package org.example.repository;

import org.example.model.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ITaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
}