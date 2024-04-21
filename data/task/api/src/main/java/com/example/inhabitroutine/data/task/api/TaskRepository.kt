package com.example.inhabitroutine.data.task.api

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskRepository {
    fun readTaskById(taskId: String): Flow<TaskModel?>
    fun readTasksByQuery(query: String): Flow<List<TaskModel>>
    suspend fun saveTask(
        taskModel: TaskModel,
        versionStartDate: LocalDate
    ): ResultModel<Unit, Throwable>

    suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable>

    suspend fun updateTaskDateById(
        taskId: String,
        taskDate: TaskDate
    ): ResultModel<Unit, Throwable>

    suspend fun updateTaskDescriptionById(
        taskId: String,
        description: String
    ): ResultModel<Unit, Throwable>

    suspend fun updateTaskIsDraftById(
        taskId: String,
        isDraft: Boolean
    ): ResultModel<Unit, Throwable>

    suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable>
}