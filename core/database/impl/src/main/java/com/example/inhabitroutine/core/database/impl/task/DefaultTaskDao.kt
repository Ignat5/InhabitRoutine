package com.example.inhabitroutine.core.database.impl.task

import com.example.inhabitroutine.core.database.api.db.TaskDao
import com.example.inhabitroutine.core.database.api.model.derived.TaskWithContentEntity
import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.util.readOneOrNull
import com.example.inhabitroutine.core.database.impl.util.toTaskWithContentEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultTaskDao(
    private val db: InhabitRoutineDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : TaskDao {

    private val taskDao = db.taskDaoQueries

    override fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?> =
        taskDao.selectTaskWithContentById(taskId).readOneOrNull(ioDispatcher).map {
            withContext(ioDispatcher) {
                it?.toTaskWithContentEntity()
            }
        }

}