package com.example.inhabitroutine.data.task.impl.repository.repository

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskModel
import com.example.inhabitroutine.domain.model.task.TaskModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultTaskRepository(
    private val taskDataSource: TaskDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : TaskRepository {

    override fun readTaskById(taskId: String): Flow<TaskModel?> =
        taskDataSource.readTaskById(taskId).map { taskEntity ->
            if (taskEntity != null) {
                withContext(defaultDispatcher) {
                    taskEntity.toTaskModel()
                }
            } else null
        }

    suspend fun saveTask(taskModel: TaskModel): ResultModel<Unit, Throwable> =
        taskModel.toTaskDataModel().let { taskDataModel ->
            taskDataSource.saveTask(taskDataModel)
        }
}