package com.ignatlegostaev.inhabitroutine.data.reminder.impl.data_source

import com.ignatlegostaev.inhabitroutine.core.database.reminder.api.ReminderDao
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.model.ReminderDataModel
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.util.encodeToEpochDay
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.util.toReminderDataModel
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.util.toReminderEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

internal class DefaultReminderDataSource(
    private val reminderDao: ReminderDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val json: Json
) : ReminderDataSource {

    override fun readRemindersByTaskId(taskId: String): Flow<List<ReminderDataModel>> =
        reminderDao.readRemindersByTaskId(taskId).map { allReminders ->
            withContext(ioDispatcher) {
                allReminders.map {
                    async { it.toReminderDataModel(json) }
                }.awaitAll().filterNotNull()
            }
        }

    override fun readReminderById(reminderId: String): Flow<ReminderDataModel?> =
        reminderDao.readReminderById(reminderId).map {
            withContext(ioDispatcher) {
                it?.toReminderDataModel(json)
            }
        }

    override fun readReminderCountByTaskId(taskId: String): Flow<Int> =
        reminderDao.readReminderCountByTaskId(taskId)

    override fun readRemindersByDate(targetDate: LocalDate): Flow<List<ReminderDataModel>> =
        reminderDao.readRemindersByDate(targetDate.encodeToEpochDay()).map { allReminders ->
            if (allReminders.isNotEmpty()) {
                withContext(ioDispatcher) {
                    allReminders.map {
                        async { it.toReminderDataModel(json) }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readReminders(): Flow<List<ReminderDataModel>> =
        reminderDao.readReminders().map { allReminders ->
            if (allReminders.isNotEmpty()) {
                withContext(ioDispatcher) {
                    allReminders.map {
                        async { it.toReminderDataModel(json) }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readReminderIdsByTaskId(taskId: String): Flow<List<String>> =
        reminderDao.readReminderIdsByTaskId(taskId)

    override fun readReminderIds(): Flow<List<String>> =
        reminderDao.readReminderIds()

    override suspend fun saveReminder(reminderDataModel: ReminderDataModel): ResultModel<Unit, Throwable> =
        withContext(ioDispatcher) {
            reminderDataModel.toReminderEntity(json)?.let {
                reminderDao.saveReminder(it)
            } ?: ResultModel.failure(IllegalStateException())
        }

    override suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable> =
        reminderDao.deleteReminderById(reminderId)
}