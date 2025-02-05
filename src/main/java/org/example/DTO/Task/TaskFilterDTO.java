package org.example.DTO.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.task.TaskPriority;
import org.example.model.task.TaskStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilterDTO {
    private String header;
    private TaskStatus status;
    private TaskPriority priority;
    private int page;
    @Builder.Default
    private int size = 10;
}
