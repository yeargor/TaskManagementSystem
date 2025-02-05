package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.model.task.DTO.TaskCreateDTO;
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

    public Task createTask(TaskCreateDTO taskCreateDTO) {
        User currentUser = userService.getCurrentUser();

        Task task = new Task();
        task.setHeader(taskCreateDTO.getHeader());
        task.setDescription(taskCreateDTO.getDescription());
        task.setStatus(taskCreateDTO.getStatus());
        task.setPriority(taskCreateDTO.getPriority());
        task.setAuthor(currentUser);

        Set<User> executors = new HashSet<>();
        if (taskCreateDTO.getExecutorIds() != null) {
            for (String executorId : taskCreateDTO.getExecutorIds()) {
                userService.findById(executorId).ifPresent(executors::add);
            }
        }
        task.setExecutors(executors);

        return taskRepository.save(task);
    }

    public List<Task> getAllMyTasks(TaskFilterDTO filterDto) {
        String currentUserId = userService.getCurrentUser().getId();

        Specification<Task> securitySpec = TaskSecuritySpecification.belongsToUser(currentUserId);
        Specification<Task> additionalSpec = TaskAdditionalSpecification.getSpecification(filterDto);

        Specification<Task> finalSpec = securitySpec.and(additionalSpec);

        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize());

        Page<Task> taskPage = taskRepository.findAll(finalSpec, pageable);
        return taskPage.getContent();
    }

    public Task updateTask(Integer taskId, TaskCreateDTO taskCreateDTO) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Задача с id " + taskId + " не найдена"));

        existingTask.setHeader(taskCreateDTO.getHeader());
        existingTask.setDescription(taskCreateDTO.getDescription());
        existingTask.setStatus(taskCreateDTO.getStatus());
        existingTask.setPriority(taskCreateDTO.getPriority());

        Set<User> executors = new HashSet<>();
        if (taskCreateDTO.getExecutorIds() != null) {
            for (String executorId : taskCreateDTO.getExecutorIds()) {
                userService.findById(executorId).ifPresent(executors::add);
            }
        }
        existingTask.setExecutors(executors);

        return taskRepository.save(existingTask);
    }

    public void deleteTaskById(Integer taskId) {
        taskRepository.deleteById(taskId);
    }

    public List<Task> getTasksByAuthor(String userId, TaskFilterDTO filterDto) {
        Specification<Task> authorSpec = TaskSecuritySpecification.byAuthor(userId);
        Specification<Task> additionalSpec = TaskAdditionalSpecification.getSpecification(filterDto);
        Specification<Task> finalSpec = authorSpec.and(additionalSpec);

        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize());
        Page<Task> taskPage = taskRepository.findAll(finalSpec, pageable);
        return taskPage.getContent();
    }

    public List<Task> getTasksByExecutor(String userId, TaskFilterDTO filterDto) {
        Specification<Task> executorSpec = TaskSecuritySpecification.byExecutor(userId);
        Specification<Task> additionalSpec = TaskAdditionalSpecification.getSpecification(filterDto);
        Specification<Task> finalSpec = executorSpec.and(additionalSpec);

        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize());
        Page<Task> taskPage = taskRepository.findAll(finalSpec, pageable);
        return taskPage.getContent();
    }
}
