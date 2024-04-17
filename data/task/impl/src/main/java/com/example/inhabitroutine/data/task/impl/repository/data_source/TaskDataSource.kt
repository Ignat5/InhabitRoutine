package com.example.inhabitroutine.data.task.impl.repository.data_source

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import kotlinx.coroutines.flow.Flow

interface TaskDataSource {
    fun readTaskById(taskId: String): Flow<TaskDataModel?>
    suspend fun saveTask(taskDataModel: TaskDataModel): ResultModel<Unit, Throwable>
    suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable>
    suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable>
}