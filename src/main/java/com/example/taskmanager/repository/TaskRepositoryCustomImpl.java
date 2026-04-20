
package com.example.taskmanager.repository;

import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.model.entity.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<Task> findByParams(TaskStatus status, Long authorId, Long assigneeId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();

        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }

        if (authorId != null) {
            predicates.add(cb.equal(root.get("author").get("id"), authorId));
        }

        if (assigneeId != null) {
            predicates.add(cb.equal(root.get("assignee").get("id"), assigneeId));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
}