package com.example.inhabitroutine.core.database.task.api

import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.flow.Flow

interface TaskDao {
    fun readTaskById(taskId: String): Flow<TaskEntity?>
    suspend fun saveTask(taskEntity: TaskEntity): ResultModel<Unit, Throwable>
}