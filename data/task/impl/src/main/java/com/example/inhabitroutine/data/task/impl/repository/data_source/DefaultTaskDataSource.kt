package com.example.inhabitroutine.data.task.impl.repository.data_source

import com.example.inhabitroutine.core.database.task.api.TaskDao
import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class DefaultTaskDataSource(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val json: Json
) : TaskDataSource {

    override fun readTaskById(taskId: String): Flow<TaskDataModel?> =
        taskDao.readTaskById(taskId).map { taskEntity ->
            withContext(ioDispatcher) {
                taskEntity?.toTaskDataModel(json)
            }
        }

    override suspend fun saveTask(taskDataModel: TaskDataModel): ResultModel<Unit, Throwable> =
        withContext(ioDispatcher) {
            taskDataModel.toTaskEntity(json)?.let { taskEntity ->
                taskDao.saveTask(taskEntity)
            } ?: ResultModel.failure(IllegalStateException())
        }
}