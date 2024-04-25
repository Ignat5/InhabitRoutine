package com.example.inhabitroutine.core.database.task.api

import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.flow.Flow

interface TaskDao {
    fun readTaskById(taskId: String): Flow<TaskEntity?>
    fun readTasksByQuery(query: String): Flow<List<TaskEntity>>
    fun readTasksByDate(targetEpochDay: Long): Flow<List<TaskEntity>>
    suspend fun saveTask(taskEntity: TaskEntity): ResultModel<Unit, Throwable>
    suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable>

    suspend fun updateTaskStartEndDateById(
        taskId: String,
        startEpochDay: Long,
        endEpochDay: Long
    ): ResultModel<Unit, Throwable>

    suspend fun updateTaskDescriptionById(
        taskId: String,
        description: String
    ): ResultModel<Unit, Throwable>

    suspend fun updateTaskIsDraftById(
        taskId: String,
        isDraft: String
    ): ResultModel<Unit, Throwable>

    suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable>
}