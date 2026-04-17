package com.example.taskmanager.model.request.task;

import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotNull
    private TaskStatus status;
    @NotNull
    private TaskPriority priority;
}

