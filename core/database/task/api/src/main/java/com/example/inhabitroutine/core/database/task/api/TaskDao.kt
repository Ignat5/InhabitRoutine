package com.example.inhabitroutine.core.database.task.api

import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.flow.Flow

interface TaskDao {
    fun readTaskById(taskId: String): Flow<TaskEntity?>
    fun readTasksByQuery(query: String): Flow<List<TaskEntity>>
    fun readTasksByDate(targetEpochDay: Long): Flow<List<TaskEntity>>
    fun readTasksById(taskId: String): Flow<List<TaskEntity>>
    fun readTasksByTaskType(targetTaskTypes: Set<String>): Flow<List<TaskEntity>>
    suspend fun saveTask(taskEntity: TaskEntity): ResultModel<Unit, Throwable>
    suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable>
}