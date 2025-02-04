package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.model.task.TaskComment;
import org.example.repository.ITaskCommentRepository;
import org.example.repository.ITaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.example.model.task.Task;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCommentService {
    private final ITaskRepository taskRepository;
    private final ITaskCommentRepository taskCommentRepository;
    private final UserService userService;

    public TaskComment createTaskComment(Integer taskId, String content) {
        // Ищем задачу по заданному идентификатору
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Задача с id " + taskId + " не найдена"));

        // Получаем текущего пользователя (автора комментария)
        User currentUser = userService.getCurrentUser();

        boolean isRelated = task.getExecutors().contains(currentUser);
        boolean isAdmin = userService.currentUserHasRole("ADMIN");

        if (!isRelated && !isAdmin) {
            throw new SecurityException("Вы не можете оставить комментарий к этому таску");
        }

        // Создаем новый комментарий
        TaskComment comment = new TaskComment();
        comment.setTask(task);
        comment.setContent(content);
        comment.setAuthor(currentUser);

        // Сохраняем комментарий в базе
        return taskCommentRepository.save(comment);
    }

    public void deleteTaskCommentById(Integer commentId) {
        TaskComment comment = taskCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий с id " + commentId + " не найден"));

        // Получаем текущего пользователя
        User currentUser = userService.getCurrentUser();

        // Проверяем, является ли текущий пользователь автором комментария или администратором
        boolean isAuthor = comment.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = userService.currentUserHasRole("ADMIN");

        if (!isAuthor && !isAdmin) {
            throw new SecurityException("Вы не можете удалить этот комментарий");
        }

        taskCommentRepository.delete(comment);
    }

    public Page<TaskComment> getTaskCommentsByTaskId(Integer taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return taskCommentRepository.findByTaskId(taskId, pageable);
    }

    public TaskComment getTaskCommentById(Integer id) {
        return taskCommentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий с id " + id + " не найден"));
    }
}
