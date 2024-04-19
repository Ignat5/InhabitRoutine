package com.example.inhabitroutine.data.reminder.impl.repository

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.data.reminder.impl.data_source.ReminderDataSource
import com.example.inhabitroutine.data.reminder.impl.util.toReminderModel
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultReminderRepository(
    private val reminderDataSource: ReminderDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : ReminderRepository {

    override fun readRemindersByTaskId(taskId: String): Flow<List<ReminderModel>> =
        reminderDataSource.readRemindersByTaskId(taskId).map { allReminders ->
            withContext(defaultDispatcher) {
                allReminders.map { it.toReminderModel() }
            }
        }

    override fun readReminderCountByTaskId(taskId: String): Flow<Int> =
        reminderDataSource.readReminderCountByTaskId(taskId)

}