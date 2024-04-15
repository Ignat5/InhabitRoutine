package com.example.inhabitroutine.data.task.impl.repository.data_source

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {
    fun readTaskById(taskId: String): Flow<TaskDataModel?>
    suspend fun saveTask(taskDataModel: TaskDataModel): ResultModel<Unit, Throwable>
}