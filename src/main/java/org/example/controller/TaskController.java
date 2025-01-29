package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.tasks.Task;
import org.example.service.tasks.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Task updateTask(@RequestBody Task task) {
        taskService.saveTask(task);
        return task;
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Task deleteTask(@RequestBody Task task) {
        taskService.deleteTask(task);
        return task;
    }
}
