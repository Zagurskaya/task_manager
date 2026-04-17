package com.example.taskmanager.service;

import com.example.taskmanager.model.dto.TaskDto;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface TaskService {

    TaskDto create(TaskDto dto, Authentication auth);

    TaskDto getById(Long id);

    TaskDto update(Long id, TaskDto taskDto, Authentication auth);

    void deleteById(Long id, Authentication auth);

    List<TaskDto> getByParam(String status, Long authorId, Long assigneeId);

}
