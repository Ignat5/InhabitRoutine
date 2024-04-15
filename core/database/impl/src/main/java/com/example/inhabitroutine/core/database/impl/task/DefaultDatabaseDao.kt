package com.example.inhabitroutine.core.database.impl.task

import com.example.inhabitroutine.core.database.api.db.DatabaseDao
import com.example.inhabitroutine.core.database.api.model.core.ReminderEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskContentEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskEntity
import com.example.inhabitroutine.core.database.api.model.derived.TaskWithContentEntity
import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.util.readOneOrNull
import com.example.inhabitroutine.core.database.impl.util.runTransaction
import com.example.inhabitroutine.core.database.impl.util.toReminderTable
import com.example.inhabitroutine.core.database.impl.util.toTaskContentTable
import com.example.inhabitroutine.core.database.impl.util.toTaskTable
import com.example.inhabitroutine.core.database.impl.util.toTaskWithContentEntity
import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultDatabaseDao(
    private val db: InhabitRoutineDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : DatabaseDao {

    private val taskDao = db.taskDaoQueries

    override suspend fun saveTaskWithContent(
        taskEntity: TaskEntity,
        taskContentEntity: TaskContentEntity,
        reminderEntity: ReminderEntity?
    ): ResultModel<Unit, Throwable> = db.runTransaction(ioDispatcher) {
        taskDao.apply {
            insertTask(taskEntity.toTaskTable())
            insertTaskContent(taskContentEntity.toTaskContentTable())
            reminderEntity?.toReminderTable()?.let { reminderTable ->
                insertReminder(reminderTable)
            }
        }
    }

    override fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?> =
        taskDao.selectTaskWithContentById(taskId).readOneOrNull(ioDispatcher).map {
            withContext(ioDispatcher) {
                it?.toTaskWithContentEntity()
            }
        }

}