package com.example.inhabitroutine.data.reminder.impl.data_source

import com.example.inhabitroutine.core.database.reminder.api.ReminderDao
import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.reminder.impl.model.ReminderDataModel
import com.example.inhabitroutine.data.reminder.impl.util.toReminderDataModel
import com.example.inhabitroutine.data.reminder.impl.util.toReminderEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

internal class DefaultReminderDataSource(
    private val reminderDao: ReminderDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val json: Json
) : ReminderDataSource {

    override fun readRemindersByTaskId(taskId: String): Flow<List<ReminderDataModel>> =
        reminderDao.readRemindersByTaskId(taskId).map { allReminders ->
            withContext(ioDispatcher) {
                allReminders.mapNotNull { reminderEntity ->
                    reminderEntity.toReminderDataModel(json)
                }
            }
        }

    override fun readReminderCountByTaskId(taskId: String): Flow<Int> =
        reminderDao.readReminderCountByTaskId(taskId)

    override suspend fun saveReminder(reminderDataModel: ReminderDataModel): ResultModel<Unit, Throwable> =
        withContext(ioDispatcher) {
            reminderDataModel.toReminderEntity(json)?.let {
                reminderDao.saveReminder(it)
            } ?: ResultModel.failure(IllegalStateException())
        }

    override suspend fun updateReminder(reminderDataModel: ReminderDataModel): ResultModel<Unit, Throwable> =
        withContext(ioDispatcher) {
            reminderDataModel.toReminderEntity(json)?.let {
                reminderDao.updateReminder(it)
            } ?: ResultModel.failure(IllegalStateException())
        }

    override suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable> =
        reminderDao.deleteReminderById(reminderId)
}