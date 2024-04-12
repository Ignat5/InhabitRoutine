package com.example.inhabitroutine.datasource.task.impl

import com.example.inhabitroutine.core.database.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.readOneOrNull
import com.example.inhabitroutine.data.model.task.TaskEntity
import com.example.inhabitroutine.datasource.task.api.TaskDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DefaultTaskDataSource(
    private val db: InhabitRoutineDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val json: Json
) : TaskDataSource {

    private val taskDao = db.taskDaoQueries

    override fun readTaskById(taskId: String): Flow<TaskEntity?> {
        taskDao.selectTaskWithContentById(taskId)
            .readOneOrNull(ioDispatcher).map {

            }
        return flow { emit(null) }
    }

}