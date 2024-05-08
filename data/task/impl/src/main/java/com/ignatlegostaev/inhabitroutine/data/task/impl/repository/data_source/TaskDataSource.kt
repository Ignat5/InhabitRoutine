package com.ignatlegostaev.inhabitroutine.data.task.impl.repository.data_source

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskDataSource {
    fun readTaskById(taskId: String): Flow<TaskDataModel?>
    fun readTasksByQuery(query: String): Flow<List<TaskDataModel>>
    fun readTasksByDate(targetDate: LocalDate): Flow<List<TaskDataModel>>
    fun readTasksById(taskId: String): Flow<List<TaskDataModel>>
    fun readTasksByTaskType(targetTaskTypes: Set<TaskType>): Flow<List<TaskDataModel>>
    suspend fun saveTask(taskDataModel: TaskDataModel): ResultModel<Unit, Throwable>
    suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable>
}