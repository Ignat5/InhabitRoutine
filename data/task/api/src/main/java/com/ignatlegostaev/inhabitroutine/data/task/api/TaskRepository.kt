package com.ignatlegostaev.inhabitroutine.data.task.api

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskRepository {
    fun readTaskById(taskId: String): Flow<TaskModel?>
    fun readTasksByQuery(query: String): Flow<List<TaskModel>>
    fun readTasksByDate(targetDate: LocalDate): Flow<List<TaskModel>>
    fun readTasksById(taskId: String): Flow<List<TaskModel>>
    fun readTasksByTaskType(targetTaskTypes: Set<TaskType>): Flow<List<TaskModel>>
    suspend fun saveTask(
        taskModel: TaskModel
    ): ResultModel<Unit, Throwable>

    suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable>
}