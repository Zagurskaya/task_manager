package com.example.taskmanager.service;

import com.example.taskmanager.model.dto.task.CreateTaskDto;
import com.example.taskmanager.model.dto.task.TaskDto;
import com.example.taskmanager.model.dto.task.UpdateTaskDto;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface TaskService {

    TaskDto create(CreateTaskDto dto, Authentication auth);

    TaskDto getById(Long id);

    TaskDto update(Long id, UpdateTaskDto taskDto, Authentication auth);

    void deleteById(Long id, Authentication auth);

    List<TaskDto> getByParam(String status, Long authorId, Long assigneeId);

}
