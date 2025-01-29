package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.tasks.TaskComment;
import org.example.service.TaskCommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class TaskCommentController {
    private final TaskCommentService taskCommentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TaskComment createComment(@RequestParam("id") int id) {
        TaskComment comment = taskCommentService.getTaskCommentById(id);
        taskCommentService.createTaskComment(comment);
        return comment;
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TaskComment deleteComment(@RequestParam("id") int id) {
        TaskComment comment = taskCommentService.getTaskCommentById(id);
        taskCommentService.deleteTaskComment(comment);
        return comment;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('CLIENT')")
    public List<TaskComment> getAllComments() {
        return taskCommentService.getAllTaskComments();
    }
}
