package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.model.task.DTO.TaskFilterDTO;
import org.example.model.task.Task;
import org.example.repository.ITaskRepository;
import org.example.repository.specification.TaskAdditionalSpecification;
import org.example.repository.specification.TaskSecuritySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final ITaskRepository taskRepository;
    private final UserService userService;

    public Task createTask(Task task, List<String> executorIds) {
        // Устанавливаем автора как текущего пользователя
        User currentUser = userService.getCurrentUser();
        task.setAuthor(currentUser);

        // Обработка списка исполнителей
        Set<User> executors = new HashSet<>();
        if (executorIds != null) {
            for (String executorId : executorIds) {
                userService.findById(executorId).ifPresent(executors::add);
            }
        }
        task.setExecutors(executors);

        return taskRepository.save(task);
    }

    public List<Task> getFilteredTasks(TaskFilterDTO filterDto) {
        String currentUserId = userService.getCurrentUser().getId();

        Specification<Task> securitySpec = TaskSecuritySpecification.belongsToUser(currentUserId);
        Specification<Task> additionalSpec = TaskAdditionalSpecification.getSpecification(filterDto);

        Specification<Task> finalSpec = securitySpec.and(additionalSpec);

        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize());

        Page<Task> taskPage = taskRepository.findAll(finalSpec, pageable);
        return taskPage.getContent();
    }

    public Task updateTask(Integer taskId, Task task, List<String> executorIds) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Задача с id " + taskId + " не найдена"));

        // Обновляем основные поля
        existingTask.setHeader(task.getHeader());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());

        // Обновляем список исполнителей
        Set<User> executors = new HashSet<>();
        if (executorIds != null) {
            for (String executorId : executorIds) {
                userService.findById(executorId).ifPresent(executors::add);
            }
        }
        existingTask.setExecutors(executors);

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Integer taskId) {
        taskRepository.deleteById(taskId);
    }
}
