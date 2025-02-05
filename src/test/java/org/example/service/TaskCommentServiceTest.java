package org.example.service;

import org.example.model.task.Task;
import org.example.model.TaskComment;
import org.example.repository.ITaskCommentRepository;
import org.example.repository.ITaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskCommentServiceTest {

    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private ITaskCommentRepository commentRepository;

    @InjectMocks
    private TaskCommentService taskCommentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTaskComment() {
        Integer taskId = 1;
        String commentText = "This is a test comment";

        Task task = new Task();
        task.setId(taskId);

        TaskComment comment = new TaskComment();
        comment.setId(100);
        comment.setContent(commentText);
        comment.setTask(task);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(TaskComment.class))).thenReturn(comment);

        TaskComment createdComment = taskCommentService.createTaskComment(taskId, commentText);

        assertNotNull(createdComment);
        assertEquals("This is a test comment", createdComment.getContent());
        assertEquals(taskId, createdComment.getTask().getId());
        verify(taskRepository, times(1)).findById(taskId);
        verify(commentRepository, times(1)).save(any(TaskComment.class));
    }

    @Test
    void testDeleteTaskComment() {
        Integer commentId = 100;
        TaskComment comment = new TaskComment();
        comment.setId(commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        taskCommentService.deleteTaskCommentById(commentId);

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(comment);
    }
}
