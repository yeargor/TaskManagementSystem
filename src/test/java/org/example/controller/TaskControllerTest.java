package org.example.controller;

import org.example.model.task.Task;
import org.example.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void getTasks_ShouldReturnList() {
        List<Task> mockTasks = List.of(new Task(), new Task());
        when(taskService.getAllMyTasks(any())).thenReturn(mockTasks);

        ResponseEntity<List<Task>> response = taskController.getTasks(null, null, null, 0, 10);

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        verify(taskService, times(1)).getAllMyTasks(any());
    }
}
