package com.example.taskmanager.service;

import com.example.taskmanager.enums.Role;
import com.example.taskmanager.enums.TaskPriority;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.exception.ApplicationException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.dto.task.CreateTaskDto;
import com.example.taskmanager.model.dto.task.TaskDto;
import com.example.taskmanager.model.dto.task.UpdateTaskDto;
import com.example.taskmanager.model.dto.user.UserDto;
import com.example.taskmanager.model.entity.Task;
import com.example.taskmanager.model.entity.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.impl.TaskServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TaskServiceImpl.class})
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TaskMapper taskMapper;
    @MockBean
    private Authentication auth;

    @Autowired
    private TaskService taskService;

    @Test
    void create_success() {

        Long userId = 1L;
        String userName = "user";
        String email = "user@mail.com";
        String password = "pass";
        Role role = Role.USER;

        User user = User.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .password(password)
                .role(role)
                .build();
        UserDto userDto = UserDto.builder()
                .id(userId)
                .build();

        String title = "title";
        String description = "description";
        TaskStatus status = TaskStatus.TODO;
        TaskPriority taskPriority = TaskPriority.MEDIUM;

        CreateTaskDto dto = CreateTaskDto.builder()
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .build();
        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .assignee(user)
                .author(user)
                .build();

        TaskDto taskDto = TaskDto.builder()
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .assignee(userDto)
                .author(userDto)
                .build();

        when(auth.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(taskMapper.toEntity(dto.getTitle(), dto.getDescription(), dto.getStatus(), dto.getPriority(), user, user))
                .thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.create(dto, auth);

        assertNotNull(result);
        assertEquals(result.getTitle(), taskDto.getTitle());
        assertEquals(result.getDescription(), taskDto.getDescription());
        assertEquals(result.getStatus(), taskDto.getStatus());
        assertEquals(result.getPriority(), taskDto.getPriority());

        verify(taskRepository).save(task);
    }


    @Test
    void getById_success() {

        Long userId = 1L;
        String userName = "user";
        String email = "user@mail.com";
        String password = "pass";
        Role role = Role.USER;

        User user = User.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .password(password)
                .role(role)
                .build();
        UserDto userDto = UserDto.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .role(role)
                .build();

        Long taskId = 1L;
        String title = "title";
        String description = "description";
        TaskStatus status = TaskStatus.TODO;
        TaskPriority taskPriority = TaskPriority.MEDIUM;

        Task task = Task.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .author(user)
                .assignee(user)
                .build();

        TaskDto taskDto = TaskDto.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .author(userDto)
                .assignee(userDto)
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto result = taskService.getById(taskId);

        assertNotNull(result);
        assertEquals(result.getTitle(), taskDto.getTitle());
        assertEquals(result.getDescription(), taskDto.getDescription());
        assertEquals(result.getStatus(), taskDto.getStatus());
        assertEquals(result.getPriority(), taskDto.getPriority());

        verify(taskRepository).findById(taskId);
    }

    @Test
    void update_success() {

        Long userId = 1L;
        String userName = "user";
        String email = "user@mail.com";
        String password = "pass";
        Role role = Role.USER;

        User user = User.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .password(password)
                .role(role)
                .build();
        UserDto userDto = UserDto.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .role(role)
                .build();

        Long taskId = 1L;
        String title = "title";
        String newTitle = "newTitle";
        String description = "description";
        String newDescription = "newDescription";
        TaskStatus status = TaskStatus.TODO;
        TaskPriority taskPriority = TaskPriority.MEDIUM;

        Task task = Task.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .assignee(user)
                .author(user)
                .build();
        Task newTask = Task.builder()
                .id(taskId)
                .title(newTitle)
                .description(newDescription)
                .status(status)
                .priority(taskPriority)
                .assignee(user)
                .author(user)
                .build();

        UpdateTaskDto updateTaskDto = UpdateTaskDto.builder()
                .title(newTitle)
                .description(newDescription)
                .status(status)
                .priority(taskPriority)
                .assignee(userDto)
                .build();

        TaskDto taskDto = TaskDto.builder()
                .id(taskId)
                .title(newTitle)
                .description(newDescription)
                .status(status)
                .priority(taskPriority)
                .assignee(userDto)
                .author(userDto)
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(auth.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(newTask);
        when(taskMapper.toDto(newTask)).thenReturn(taskDto);

        TaskDto result = taskService.update(taskId, updateTaskDto, auth);

        assertNotNull(result);
        assertEquals(result.getTitle(), taskDto.getTitle());
        assertEquals(result.getDescription(), taskDto.getDescription());
        assertEquals(result.getStatus(), taskDto.getStatus());
        assertEquals(result.getPriority(), taskDto.getPriority());
        assertEquals(result.getAssignee().getId(), taskDto.getAssignee().getId());
        assertEquals(result.getAuthor().getId(), taskDto.getAuthor().getId());

        verify(taskRepository).save(task);
    }

    @Test
    void deleteById_success() {

        Long userId = 1L;
        String userName = "user";
        String email = "user@mail.com";
        String password = "pass";
        Role role = Role.USER;

        User user = User.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .password(password)
                .role(role)
                .build();

        Long taskId = 1L;
        String title = "title";
        String description = "description";
        TaskStatus status = TaskStatus.TODO;
        TaskPriority taskPriority = TaskPriority.MEDIUM;

        Task task = Task.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .assignee(user)
                .author(user)
                .build();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(auth.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        taskService.deleteById(taskId, auth);

        verify(taskRepository).delete(task);
    }

    @Test
    void delete_admin_success() {

        Long userId = 1L;
        String userName = "user";
        String email = "user@mail.com";
        String password = "pass";
        Role role = Role.USER;

        User user = User.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .password(password)
                .role(role)
                .build();

        Long taskId = 1L;
        String title = "title";
        String description = "description";
        TaskStatus status = TaskStatus.TODO;
        TaskPriority taskPriority = TaskPriority.MEDIUM;

        Task task = Task.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .assignee(user)
                .author(user)
                .build();

        String adminEmail = "admin@mail.com";
        User admin = User.builder()
                .id(10L)
                .username("admin")
                .email(adminEmail)
                .password("pass")
                .role(Role.ADMIN)
                .build();

        when(auth.getName()).thenReturn(adminEmail);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(adminEmail)).thenReturn(Optional.of(admin));

        taskService.deleteById(taskId, auth);

        verify(taskRepository).delete(task);
    }

    @Test
    void getByParam_success() {

        Long userId = 1L;
        String userName = "user";
        String email = "user@mail.com";
        String password = "pass";
        Role role = Role.USER;

        User user = User.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .password(password)
                .role(role)
                .build();
        UserDto userDto = UserDto.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .role(role)
                .build();

        Long taskId = 1L;
        String title = "title";
        String description = "description";
        TaskStatus status = TaskStatus.TODO;
        TaskPriority taskPriority = TaskPriority.MEDIUM;

        Task task = Task.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .author(user)
                .assignee(user)
                .build();

        TaskDto taskDto = TaskDto.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .author(userDto)
                .assignee(userDto)
                .build();

        when(taskRepository.findByParams(status, userId, userId)).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        List<TaskDto> result = taskService.getByParam(status, userId, userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(result.get(0).getTitle(), taskDto.getTitle());
        assertEquals(result.get(0).getDescription(), taskDto.getDescription());
        assertEquals(result.get(0).getStatus(), taskDto.getStatus());
        assertEquals(result.get(0).getPriority(), taskDto.getPriority());

        verify(taskRepository).findByParams(status, userId, userId);
    }

    @Test
    void update_noPermission_throwsException() {

        String email = "user@mail.com";

        User author = User.builder()
                .id(1L)
                .username("author")
                .email("author@mail.com")
                .password("pass")
                .role(Role.USER)
                .build();
        User other = User.builder()
                .id(2L)
                .username("other")
                .email(email)
                .password("pass")
                .role(Role.USER)
                .build();

        Long taskId = 1L;
        String title = "title";
        String description = "description";
        TaskStatus status = TaskStatus.TODO;
        TaskPriority taskPriority = TaskPriority.MEDIUM;

        Task task = Task.builder()
                .id(taskId)
                .title(title)
                .description(description)
                .status(status)
                .priority(taskPriority)
                .author(author)
                .assignee(author)
                .build();

        when(auth.getName()).thenReturn(email);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(other));

        UpdateTaskDto dto = new UpdateTaskDto();

        assertThrows(ApplicationException.class, () -> taskService.update(taskId, dto, auth));
    }

}
