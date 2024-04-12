package com.example.inhabitroutine.data.task.impl.repository

import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.datasource.task.api.TaskDataSource

class DefaultTaskRepository(
    private val taskDataSource: TaskDataSource
): TaskRepository {
}