package com.example.inhabitroutine.data.reminder.impl.data_source

import com.example.inhabitroutine.core.database.reminder.api.ReminderDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class DefaultReminderDataSource(
    private val reminderDao: ReminderDao,
    private val ioDispatcher: CoroutineDispatcher
) : ReminderDataSource {

    override fun readReminderCountByTaskId(taskId: String): Flow<Int> =
        reminderDao.readReminderCountByTaskId(taskId)

}