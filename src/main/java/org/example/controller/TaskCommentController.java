package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.DTO.CommentDTO;
import org.example.service.TaskCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/{taskId}/comments")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<?> createComment(@PathVariable Integer taskId,
                                     @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(taskCommentService.createTaskComment(taskId, commentDTO.getContent()));
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        taskCommentService.deleteTaskCommentById(commentId);
        return ResponseEntity.ok("Комментарий удалён");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<?> getAllComments(
            @PathVariable Integer taskId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        return ResponseEntity.ok(taskCommentService.getTaskCommentsByTaskId(taskId, page, size));
    }

    @GetMapping("/{commentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<?> getComment(@PathVariable Integer commentId) {
        return ResponseEntity.ok(taskCommentService.getTaskCommentById(commentId));
    }
}
