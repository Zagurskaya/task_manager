package com.example.taskmanager.repository;

import com.example.taskmanager.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends TaskRepositoryCustom, JpaRepository<Task, Long> {

}

