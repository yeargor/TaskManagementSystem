package org.example.DTO.Task;

import lombok.*;
import org.example.model.task.TaskPriority;
import org.example.model.task.TaskStatus;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {
    private String header;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private List<String> executorIds;
}
