package com.example.inhabitroutine.data.task.impl.repository.data_source

import com.example.inhabitroutine.core.database.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.readOneOrNull
import com.example.inhabitroutine.data.task.impl.repository.model.TaskEntity
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class DefaultTaskDataSource(
    private val db: InhabitRoutineDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val json: Json
) : TaskDataSource {

    private val taskDao = db.taskDaoQueries

    override fun readTaskById(taskId: String): Flow<TaskEntity?> =
        taskDao.selectTaskWithContentById(taskId).readOneOrNull(ioDispatcher)
            .map { taskWithContentView ->
                withContext(ioDispatcher) {
                    taskWithContentView?.toTaskEntity(json)
                }
            }
}