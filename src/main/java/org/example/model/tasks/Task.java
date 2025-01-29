package org.example.model.tasks;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Task {
    @Id
    private Long id;
    private String header;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskComment> comment;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToMany
    @JoinTable(
            name = "task_executors",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "executor_id")
    )
    private Set<User> executors = new HashSet<>();
}
