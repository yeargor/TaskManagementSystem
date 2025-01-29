package org.example.model.tasks;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskComment {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    private String content;
}
