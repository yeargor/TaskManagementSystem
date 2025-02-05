package org.example.model.task;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.model.User;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "author_id", columnDefinition = "TEXT", nullable = false)
    private User author;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    private String content;
}
