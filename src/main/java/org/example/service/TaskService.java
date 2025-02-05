package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.User;
import org.example.DTO.Task.TaskCreateDTO;
import org.example.DTO.Task.TaskFilterDTO;
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
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ITaskRepository taskRepository;
    private final UserService userService;

    public Task createTask(TaskCreateDTO taskCreateDTO) {
        User currentUser = userService.getCurrentUser();

        Task task = new Task();
        updateTaskFromDTO(task, taskCreateDTO);
        task.setAuthor(currentUser);

        return taskRepository.save(task);
    }

    public Task updateTask(Integer taskId, TaskCreateDTO taskCreateDTO) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with id " + taskId + " not found"));

        updateTaskFromDTO(existingTask, taskCreateDTO);
        return taskRepository.save(existingTask);
    }

    private void updateTaskFromDTO(Task task, TaskCreateDTO taskCreateDTO) {
        task.setHeader(taskCreateDTO.getHeader());
        task.setDescription(taskCreateDTO.getDescription());
        task.setStatus(taskCreateDTO.getStatus());
        task.setPriority(taskCreateDTO.getPriority());
        task.setExecutors(findExecutorsByIds(taskCreateDTO.getExecutorIds()));
    }

    private Set<User> findExecutorsByIds(List<String> executorIds) {
        Set<User> executors = new HashSet<>();
        if (executorIds != null) {
            for (String executorId : executorIds) {
                Optional<User> user = userService.findById(executorId);
                user.ifPresent(executors::add);
            }
        }
        return executors;
    }

    public List<Task> getAllMyTasks(TaskFilterDTO filterDto) {
        String currentUserId = userService.getCurrentUser().getId();
        return getFilteredTasks(TaskSecuritySpecification.belongsToUser(currentUserId), filterDto);
    }

    public List<Task> getTasksByAuthor(String userId, TaskFilterDTO filterDto) {
        return getFilteredTasks(TaskSecuritySpecification.byAuthor(userId), filterDto);
    }

    public List<Task> getTasksByExecutor(String userId, TaskFilterDTO filterDto) {
        return getFilteredTasks(TaskSecuritySpecification.byExecutor(userId), filterDto);
    }

    private List<Task> getFilteredTasks(Specification<Task> securitySpec, TaskFilterDTO filterDto) {
        Specification<Task> finalSpec = securitySpec.and(TaskAdditionalSpecification.getSpecification(filterDto));
        Pageable pageable = PageRequest.of(filterDto.getPage(), filterDto.getSize());
        Page<Task> taskPage = taskRepository.findAll(finalSpec, pageable);
        return taskPage.getContent();
    }

    public void deleteTaskById(Integer taskId) {
        taskRepository.deleteById(taskId);
    }
}
