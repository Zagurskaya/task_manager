package com.example.taskmanager.repository;

import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.model.entity.Task;
import java.util.List;

public interface TaskRepositoryCustom {
    
    List<Task> findByParams(TaskStatus status, Long authorId, Long assigneeId);
}