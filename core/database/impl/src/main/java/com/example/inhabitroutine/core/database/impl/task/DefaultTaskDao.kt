package com.example.inhabitroutine.core.database.impl.task

import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.util.readOneOrNull
import com.example.inhabitroutine.core.database.impl.util.runQuery
import com.example.inhabitroutine.core.database.impl.util.toTaskEntity
import com.example.inhabitroutine.core.database.impl.util.toTaskTable
import com.example.inhabitroutine.core.database.task.api.TaskDao
import com.example.inhabitroutine.core.database.task.api.TaskEntity
import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultTaskDao(
    private val db: InhabitRoutineDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : TaskDao {

    private val taskDao = db.taskDaoQueries

    override fun readTaskById(taskId: String): Flow<TaskEntity?> =
        taskDao.selectTaskById(taskId).readOneOrNull(ioDispatcher).map { taskTable ->
            taskTable?.toTaskEntity()
        }

    override suspend fun saveTask(taskEntity: TaskEntity): ResultModel<Unit, Throwable> =
        runQuery(ioDispatcher) {
            taskDao.insertTask(taskEntity.toTaskTable())
        }
}