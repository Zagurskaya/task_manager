package com.example.taskmanager.controller;

import static com.example.taskmanager.constant.ExceptionMessageConstant.EMPTY_TASK_FILTER;
import static com.example.taskmanager.constant.ExceptionMessageConstant.EMPTY_UPDATE_REQUEST;

import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.exception.ValidationException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.dto.task.TaskDto;
import com.example.taskmanager.model.dto.task.UpdateTaskDto;
import com.example.taskmanager.model.request.task.CreateTaskRequest;
import com.example.taskmanager.model.request.task.UpdateTaskRequest;
import com.example.taskmanager.model.response.TaskResponse;
import com.example.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Operation(
            summary = "Создание задачи",
            description = """
                    Создаёт новую задачу.
                    Требуется авторизация. Доступ разрешён ролям USER и ADMIN.
                    """,
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Задача успешно создана",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TaskResponse create(
            @Parameter(description = "Данные для создания задачи")
            @Valid @RequestBody CreateTaskRequest request,
            Authentication auth) {

        TaskDto taskDto = taskService.create(taskMapper.toDto(request), auth);
        return taskMapper.toTaskResponse(taskDto);
    }

    @Operation(
            summary = "Получение задачи",
            description = "Возвращает задачу по её ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Задача найдена",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    @GetMapping("/{id}")
    public TaskResponse getById(
            @Parameter(description = "ID задачи")
            @PathVariable("id") Long id) {

        TaskDto taskDto = taskService.getById(id);
        return taskMapper.toTaskResponse(taskDto);
    }

    @Operation(
            summary = "Изменение задачи",
            description = """
                    Позволяет изменить существующую задачу.
                    Можно обновить:
                    • тему (title)
                    • описание (description)
                    • статус (TODO, IN_PROGRESS, DONE)
                    • приоритетность (LOW, MEDIUM, HIGH)
                    • исполнителя (assignee)
                    
                    Требуется авторизация. Доступ разрешён ролям USER и ADMIN.
                    """,
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Задача успешно обновлена",
                            content = @Content(schema = @Schema(implementation = TaskResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Нет прав для изменения задачи"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    @PutMapping("/{id}")
    public TaskResponse update(
            @Parameter(description = "ID задачи", example = "1")
            @PathVariable("id") Long id,
            @Parameter(description = "Данные для обновления задачи")
            @RequestBody UpdateTaskRequest request,
            Authentication auth) {

        if (request.isEmpty()) {
            throw new ValidationException(EMPTY_UPDATE_REQUEST);
        }
        UpdateTaskDto updateTaskDto = taskMapper.toDto(request);
        TaskDto taskDto = taskService.update(id, updateTaskDto, auth);
        return taskMapper.toTaskResponse(taskDto);
    }

    @Operation(
            summary = "Удаление задачи",
            description = """
                    Удаляет задачу по её ID.
                    Требуется авторизация. Доступ разрешён ролям USER и ADMIN.
                    """,
            security = @SecurityRequirement(name = "BearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Нет прав для удаления задачи"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID задачи", example = "1")
            @PathVariable("id") Long id,
            Authentication auth) {

        taskService.deleteById(id, auth);
    }

    @Operation(
            summary = "Получение списка задач по параметрам",
            description = """
                    Позволяет фильтровать задачи по:
                    • статусу
                    • ID автора
                    • ID исполнителя
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список задач по фильтру",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))

                    ),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            }
    )
    @GetMapping
    public List<TaskResponse> getTasksByParam(
            @Parameter(
                    description = "Статус задачи (TODO, IN_PROGRESS, DONE)",
                    schema = @Schema(implementation = TaskStatus.class)
            )
            @RequestParam(value = "status", required = false) TaskStatus status,
            @Parameter(description = "ID автора задачи")
            @RequestParam(value = "authorId", required = false) Long authorId,
            @Parameter(description = "ID исполнителя задачи")
            @RequestParam(value = "assigneeId", required = false) Long assigneeId) {

        if (status == null && authorId == null && assigneeId == null) {
            throw new ValidationException(EMPTY_TASK_FILTER);
        }
        List<TaskDto> taskDtos = taskService.getByParam(status, authorId, assigneeId);
        return taskDtos.stream()
                .map(taskMapper::toTaskResponse)
                .toList();
    }

}

