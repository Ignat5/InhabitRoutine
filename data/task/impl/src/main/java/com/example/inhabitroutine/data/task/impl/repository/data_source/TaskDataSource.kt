package com.example.inhabitroutine.data.task.impl.repository.data_source

import com.example.inhabitroutine.data.task.impl.repository.model.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {
    fun readTaskById(taskId: String): Flow<TaskEntity?>
}