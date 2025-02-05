package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.task.DTO.TaskCreateDTO;
import org.example.model.task.DTO.TaskFilterDTO;
import org.example.model.task.Task;
import org.example.model.task.TaskPriority;
import org.example.model.task.TaskStatus;
import org.example.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTask(@RequestBody TaskCreateDTO taskCreateDTO) {
        return ResponseEntity.ok(taskService.createTask(taskCreateDTO));
    }

    @GetMapping("/allMyTasks")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) String header,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        TaskFilterDTO filterDto = TaskFilterDTO.builder()
                .header(header)
                .status(status)
                .priority(priority)
                .page(page)
                .size(size)
                .build();
        return ResponseEntity.ok(taskService.getAllMyTasks(filterDto));
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTask(@PathVariable Integer taskId,
                                        @RequestBody TaskCreateDTO taskCreateDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskCreateDTO));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable Integer taskId) {
        taskService.deleteTaskById(taskId);
        return ResponseEntity.ok("Таск удалён");
    }

    @GetMapping("/author")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<Task>> getTasksByAuthor(
            @RequestParam String userId,
            @RequestParam(required = false) String header,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        TaskFilterDTO filterDto = TaskFilterDTO.builder()
                .header(header)
                .status(status)
                .priority(priority)
                .page(page)
                .size(size)
                .build();
        return ResponseEntity.ok(taskService.getTasksByAuthor(userId, filterDto));
    }

    @GetMapping("/executor")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<Task>> getTasksByExecutor(
            @RequestParam String userId,
            @RequestParam(required = false) String header,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        TaskFilterDTO filterDto = TaskFilterDTO.builder()
                .header(header)
                .status(status)
                .priority(priority)
                .page(page)
                .size(size)
                .build();
        return ResponseEntity.ok(taskService.getTasksByExecutor(userId, filterDto));
    }
}
