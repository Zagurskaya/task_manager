package com.example.taskmanager.service.impl;

import com.example.taskmanager.enums.Role;
import com.example.taskmanager.exception.ApplicationException;
import com.example.taskmanager.exception.NotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.dto.TaskDto;
import com.example.taskmanager.model.entity.Task;
import com.example.taskmanager.model.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public TaskDto create(TaskDto dto, Authentication auth) {

        User author = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found by email %s".formatted(auth.getName())));

        Task task = taskMapper.toEntity(dto.getTitle(),
                dto.getDescription(),
                dto.getStatus(),
                dto.getPriority(),
                author,
                author
        );

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDto getById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found by id %s".formatted(id)));
        return taskMapper.toDto(task);
    }

    @Override
    @Transactional
    public TaskDto update(Long id, TaskDto taskDto, Authentication auth) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found by id %s".formatted(id)));
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found by email %s".formatted(auth.getName())));

        if (!task.getAuthor().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ApplicationException("Not permission for update task");
        }

        if (taskDto.getTitle() != null) {
            task.setTitle(taskDto.getTitle());
        }
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
        }
        if (taskDto.getStatus() != null) {
            task.setStatus(taskDto.getStatus());
        }
        if (taskDto.getPriority() != null) {
            task.setPriority(taskDto.getPriority());
        }
        if (taskDto.getAssignee() != null) {
            User assignee = userRepository.findById(taskDto.getAssignee().getId())
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteById(Long id, Authentication auth) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found by id %s".formatted(id)));
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new NotFoundException("User not found by email %s".formatted(auth.getName())));
        if (!task.getAuthor().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ApplicationException("Not permission for delete task");
        }
        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getByParam(String status, Long authorId, Long assigneeId) {

        return taskRepository.findByParams(status, authorId, assigneeId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
