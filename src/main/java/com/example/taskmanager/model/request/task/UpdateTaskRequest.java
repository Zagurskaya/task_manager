package com.example.taskmanager.model.request.task;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import lombok.Data;

@Data
public class UpdateTaskRequest {

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private Long assigneeId;

    public boolean isEmpty() {
        return title == null &&
                description == null &&
                status == null &&
                priority == null &&
                assigneeId == null;
    }
}

