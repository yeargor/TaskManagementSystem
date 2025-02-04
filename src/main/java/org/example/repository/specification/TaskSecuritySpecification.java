package org.example.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.model.task.Task;
import org.springframework.data.jpa.domain.Specification;
import static jakarta.persistence.criteria.JoinType.LEFT;

public class TaskSecuritySpecification {
    // Фильтрация: задачи, где пользователь является автором или входит в список исполнителей
    public static Specification<Task> belongsToUser(String userId) {
        return (root, query, cb) -> {
            Predicate authorPredicate = cb.equal(root.get("author").get("id"), userId);
            // Используем LEFT JOIN для коллекции исполнителей
            Join<Object, Object> executorsJoin = root.join("executors",LEFT);
            Predicate executorPredicate = cb.equal(executorsJoin.get("id"), userId);
            return cb.or(authorPredicate, executorPredicate);
        };
    }
}
