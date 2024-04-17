package com.example.inhabitroutine.data.task.api

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.task.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskRepository {
    fun readTaskById(taskId: String): Flow<TaskModel?>
    suspend fun saveTask(
        taskModel: TaskModel,
        versionStartDate: LocalDate
    ): ResultModel<Unit, Throwable>
    suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable>
}