package com.example.inhabitroutine.data.reminder.impl.repository

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.data.reminder.impl.data_source.ReminderDataSource
import kotlinx.coroutines.flow.Flow

internal class DefaultReminderRepository(
    private val reminderDataSource: ReminderDataSource
) : ReminderRepository {

    override fun readReminderCountByTaskId(taskId: String): Flow<Int> =
        reminderDataSource.readReminderCountByTaskId(taskId)

}