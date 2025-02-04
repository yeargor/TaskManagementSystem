package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.task.DTO.TaskFilterDTO;
import org.example.model.task.Task;
import org.example.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTask(@RequestBody Task task,
                           @RequestParam(value = "executorIds", required = false) String executorIdsStr) {
        List<String> executorIds = null;
        if (executorIdsStr != null && !executorIdsStr.isEmpty()) {
            executorIds = Arrays.asList(executorIdsStr.split(","));
        }
        return ResponseEntity.ok(taskService.createTask(task, executorIds));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<Task>> getTasks(@RequestBody TaskFilterDTO filterDto) {
        return ResponseEntity.ok(taskService.getFilteredTasks(filterDto));
    }

    @PutMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTask(@PathVariable Integer taskId,
                           @RequestBody Task task,
                           @RequestParam(value = "executorIds", required = false) String executorIdsStr) {
        List<String> executorIds = null;
        if (executorIdsStr != null && !executorIdsStr.isEmpty()) {
            executorIds = Arrays.asList(executorIdsStr.split(","));
        }
        return ResponseEntity.ok(taskService.updateTask(taskId, task, executorIds));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable Integer taskId) {
        return ResponseEntity.ok("Комментарий удалён");
    }
}
