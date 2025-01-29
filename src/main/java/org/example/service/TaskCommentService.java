package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.tasks.TaskComment;
import org.example.repository.ITaskCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCommentService {
    private final ITaskCommentRepository taskCommentRepository;

    public TaskComment createTaskComment(TaskComment taskComment) {
        return taskCommentRepository.save(taskComment);
    }
    public TaskComment deleteTaskComment(TaskComment taskComment) {
        taskCommentRepository.delete(taskComment);
        return taskComment;
    }
    public List<TaskComment> getAllTaskComments() {
        return (List<TaskComment>) taskCommentRepository.findAll();
    }
    public TaskComment getTaskCommentById(Integer id) {
        return taskCommentRepository.findById(id).orElse(null);
    }
}
