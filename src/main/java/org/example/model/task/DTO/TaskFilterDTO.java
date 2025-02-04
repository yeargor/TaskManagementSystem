package org.example.model.task.DTO;

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
    private String header;        // Фильтрация по заголовку (необязательно)
    private TaskStatus status;    // Фильтр по статусу (необязательно)
    private TaskPriority priority;// Фильтр по приоритету (необязательно)
    private int page;             // Номер страницы (0 — первая страница)
    @Builder.Default
    private int size = 10;             // Размер страницы (количество записей на странице)
}
