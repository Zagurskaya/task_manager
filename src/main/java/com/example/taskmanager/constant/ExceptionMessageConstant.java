package com.example.taskmanager.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessageConstant {

    public static final String TASK_NOT_FOUND_BY_ID = "Не найдена задача по id %s ";

    public static final String USER_NOT_FOUND_BY_EMAIL = "Не найден пользователь по email %s ";

    public static final String NOT_PERMISSION_FOR_UPDATE_TASK = "Нет прав для обновления задачи ";

    public static final String NOT_PERMISSION_FOR_DELETE_TASK = "Нет прав для удаления задачи ";

    public static final String EMAIL_ALREADY_EXISTS = "Email уже существует";

    public static final String NOT_VALID_EMAIL_OR_PASSWORD = "Неверный email или пароль";

}
