package com.example.inhabitroutine.data.task.impl.repository.data_source

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskDataSource {
    fun readTaskById(taskId: String): Flow<TaskDataModel?>
    fun readTasksByQuery(query: String): Flow<List<TaskDataModel>>
    fun readTasksByDate(targetDate: LocalDate): Flow<List<TaskDataModel>>
    fun readTasksById(taskId: String): Flow<List<TaskDataModel>>
    suspend fun saveTask(taskDataModel: TaskDataModel): ResultModel<Unit, Throwable>
    suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable>

    suspend fun updateTaskStartEndDateById(
        taskId: String,
        startDate: LocalDate,
        endDate: LocalDate?
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