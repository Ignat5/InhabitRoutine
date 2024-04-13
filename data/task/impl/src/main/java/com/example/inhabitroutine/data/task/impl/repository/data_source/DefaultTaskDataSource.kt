package com.example.inhabitroutine.data.task.impl.repository.data_source

import com.example.inhabitroutine.core.database.api.db.TaskDao
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskDataModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class DefaultTaskDataSource(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val json: Json
) : TaskDataSource {

    override fun readTaskById(taskId: String): Flow<TaskDataModel?> =
        taskDao.readTaskWithContentById(taskId).map { taskWithContent ->
            withContext(ioDispatcher) {
                taskWithContent?.toTaskDataModel(json)
            }
        }
}