package com.example.inhabitroutine.core.database.impl.reminder

import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.util.readOne
import com.example.inhabitroutine.core.database.impl.util.runQuery
import com.example.inhabitroutine.core.database.reminder.api.ReminderDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DefaultReminderDao(
    private val db: InhabitRoutineDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : ReminderDao {

    private val reminderDao = db.reminderDaoQueries

    override fun readReminderCountByTaskId(taskId: String): Flow<Int> =
        reminderDao.selectReminderCountByTaskId(taskId)
            .readOne(ioDispatcher)
            .map { it.toInt() }

}