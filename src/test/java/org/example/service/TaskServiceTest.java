package org.example.service;

import org.example.model.User;
import org.example.model.task.DTO.TaskCreateDTO;
import org.example.model.task.Task;
import org.example.model.task.TaskPriority;
import org.example.model.task.TaskStatus;
import org.example.repository.ITaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private ITaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private User testUser;
    private User executorUser;
    private TaskCreateDTO taskDTO;

    @BeforeEach
    void setUp() {
        testUser = new User("1", "John", "Doe", "johndoe", "john@example.com");
        executorUser = new User("2", "Jane", "Smith", "janesmith", "jane@example.com");

        taskDTO = new TaskCreateDTO();
        taskDTO.setHeader("Test Task");
        taskDTO.setDescription("Description");
        taskDTO.setStatus(TaskStatus.InProgress);
        taskDTO.setPriority(TaskPriority.HIGH);
        taskDTO.setExecutorIds(List.of("2"));
    }

    @Test
    void createTask_ShouldSaveTaskWithExecutors() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(userService.findById("2")).thenReturn(Optional.of(executorUser));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task createdTask = taskService.createTask(taskDTO);

        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getHeader());
        assertEquals(testUser, createdTask.getAuthor());
        assertEquals(1, createdTask.getExecutors().size());
        assertTrue(createdTask.getExecutors().contains(executorUser));

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_ShouldModifyExistingTask() {
        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setHeader("Old Task");
        existingTask.setDescription("Old Description");
        existingTask.setStatus(TaskStatus.Finished);
        existingTask.setPriority(TaskPriority.LOW);
        existingTask.setExecutors(Set.of());

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        when(userService.findById("2")).thenReturn(Optional.of(executorUser));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updatedTask = taskService.updateTask(1, taskDTO);

        assertNotNull(updatedTask);
        assertEquals("Test Task", updatedTask.getHeader());
        assertEquals("Description", updatedTask.getDescription());
        assertEquals(TaskStatus.InProgress, updatedTask.getStatus());
        assertEquals(TaskPriority.HIGH, updatedTask.getPriority());
        assertEquals(1, updatedTask.getExecutors().size());
        assertTrue(updatedTask.getExecutors().contains(executorUser));

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldRemoveTask() {
        Task task = new Task();
        task.setId(1);

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTaskById(1);

        verify(taskRepository, times(1)).delete(task);
    }
}
