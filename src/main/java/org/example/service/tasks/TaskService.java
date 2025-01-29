package org.example.service.tasks;

import org.example.model.tasks.Task;
import org.example.repository.tasks.ITaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final ITaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }
    public Task deleteTask(Task task) {
        taskRepository.delete(task);
        return task;
    }
}
