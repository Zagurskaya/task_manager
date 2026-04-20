package com.example.taskmanager.service.impl;

import static com.example.taskmanager.constant.ExceptionMessageConstant.NOT_PERMISSION_FOR_DELETE_TASK;
import static com.example.taskmanager.constant.ExceptionMessageConstant.NOT_PERMISSION_FOR_UPDATE_TASK;
import static com.example.taskmanager.constant.ExceptionMessageConstant.TASK_NOT_FOUND_BY_ID;
import static com.example.taskmanager.constant.ExceptionMessageConstant.USER_NOT_FOUND_BY_EMAIL;
import static com.example.taskmanager.constant.ExceptionMessageConstant.USER_NOT_FOUND_BY_ID;

import com.example.taskmanager.enums.Role;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.exception.ApplicationException;
import com.example.taskmanager.exception.NotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.dto.task.CreateTaskDto;
import com.example.taskmanager.model.dto.task.TaskDto;
import com.example.taskmanager.model.dto.task.UpdateTaskDto;
import com.example.taskmanager.model.entity.Task;
import com.example.taskmanager.model.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.TaskService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public TaskDto create(CreateTaskDto dto, Authentication auth) {

        User author = getCurrentUser(auth.getName());

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
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND_BY_ID.formatted(id)));
        return taskMapper.toDto(task);
    }

    @Override
    @Transactional
    public TaskDto update(Long id, UpdateTaskDto taskDto, Authentication auth) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND_BY_ID.formatted(id)));
        User user = getCurrentUser(auth.getName());

        checkUpdatePermission(task, user);

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
            Long assigneeId = taskDto.getAssignee().getId();
            User assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_BY_ID.formatted(assigneeId)));
            task.setAssignee(assignee);
        }

        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteById(Long id, Authentication auth) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND_BY_ID.formatted(id)));
        User user = getCurrentUser(auth.getName());
        if (!task.getAuthor().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ApplicationException(NOT_PERMISSION_FOR_DELETE_TASK);
        }
        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> getByParam(TaskStatus status, Long authorId, Long assigneeId) {

        return taskRepository.findByParams(status, authorId, assigneeId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    private User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_BY_EMAIL.formatted(email)));
    }

    private void checkUpdatePermission(Task task, User user) {

        if (!task.getAuthor().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new ApplicationException(NOT_PERMISSION_FOR_UPDATE_TASK);
        }
    }
}
