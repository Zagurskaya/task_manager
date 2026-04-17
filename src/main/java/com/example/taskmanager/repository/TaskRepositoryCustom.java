package com.example.taskmanager.repository;

import com.example.taskmanager.model.entity.Task;
import java.util.List;

public interface TaskRepositoryCustom {
    
    List<Task> findByParams(String status, Long authorId, Long assigneeId);
}