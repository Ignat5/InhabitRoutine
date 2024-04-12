package com.example.inhabitroutine.datasource.task.api

import com.example.inhabitroutine.data.model.task.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {
    fun readTaskById(taskId: String): Flow<TaskEntity?>
}