package org.example.service.tasks;

import org.example.model.tasks.Task;
import org.example.repository.tasks.ITaskRepository;
import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final ITaskRepository taskRepository;
    private final UserService userService;

    public List<Task> getAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }
    public Task createTask(Task task) {
        userService.syncUsersFromKeycloak();
        return taskRepository.save(task); //сделать сквозную логику проверки наличия id в базе
    }
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }
    public Task deleteTask(Task task) {
        taskRepository.delete(task);
        return task;
    }
}
