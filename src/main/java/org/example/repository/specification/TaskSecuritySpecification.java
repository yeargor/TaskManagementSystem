package org.example.repository.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.example.model.task.Task;
import org.springframework.data.jpa.domain.Specification;
import static jakarta.persistence.criteria.JoinType.LEFT;

public class TaskSecuritySpecification {
    public static Specification<Task> belongsToUser(String userId) {
        return (root, query, cb) -> {
            Predicate authorPredicate = cb.equal(root.get("author").get("id"), userId);
            Join<Object, Object> executorsJoin = root.join("executors",LEFT);
            Predicate executorPredicate = cb.equal(executorsJoin.get("id"), userId);
            return cb.or(authorPredicate, executorPredicate);
        };
    }

    public static Specification<Task> byAuthor(String userId) {
        return (root, query, cb) -> cb.equal(root.get("author").get("id"), userId);
    }

    public static Specification<Task> byExecutor(String userId) {
        return (root, query, cb) -> {
            Join<Object, Object> executorsJoin = root.join("executors", LEFT);
            return cb.equal(executorsJoin.get("id"), userId);
        };
    }
}
