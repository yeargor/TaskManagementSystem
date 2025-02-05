package org.example.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.example.model.task.Task;
import org.example.DTO.Task.TaskFilterDTO;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class TaskAdditionalSpecification {
    public static Specification<Task> getSpecification(TaskFilterDTO filterDto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterDto.getHeader() != null && !filterDto.getHeader().isEmpty()) {
                predicates.add(cb.like(root.get("header"), "%" + filterDto.getHeader() + "%"));
            }
            if (filterDto.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filterDto.getStatus()));
            }
            if (filterDto.getPriority() != null) {
                predicates.add(cb.equal(root.get("priority"), filterDto.getPriority()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
