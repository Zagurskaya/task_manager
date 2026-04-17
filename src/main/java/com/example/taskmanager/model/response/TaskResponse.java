package com.example.taskmanager.model.response;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;

    private Long authorId;
    private Long assigneeId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

