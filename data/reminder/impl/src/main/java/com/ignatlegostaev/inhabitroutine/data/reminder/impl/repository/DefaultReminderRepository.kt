package com.ignatlegostaev.inhabitroutine.data.reminder.impl.repository

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.data_source.ReminderDataSource
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.model.ReminderDataModel
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.util.toReminderDataModel
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.util.toReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

internal class DefaultReminderRepository(
    private val reminderDataSource: ReminderDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : ReminderRepository {

    override fun readRemindersByTaskId(taskId: String): Flow<List<ReminderModel>> =
        reminderDataSource.readRemindersByTaskId(taskId).map { allReminders ->
            withContext(defaultDispatcher) {
                allReminders.mapToReminderModel()
            }
        }

    override fun readReminderById(reminderId: String): Flow<ReminderModel?> =
        reminderDataSource.readReminderById(reminderId).map {
            it?.toReminderModel()
        }

    override fun readReminderCountByTaskId(taskId: String): Flow<Int> =
        reminderDataSource.readReminderCountByTaskId(taskId)

    override fun readRemindersByDate(targetDate: LocalDate): Flow<List<ReminderModel>> =
        reminderDataSource.readRemindersByDate(targetDate).map { allReminders ->
            if (allReminders.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allReminders
                        .mapToReminderModel()
                        .filterByDate(targetDate)
                }
            } else emptyList()
        }

    private fun List<ReminderModel>.filterByDate(date: LocalDate) = this.let { allReminders ->
        allReminders.filter { reminderModel ->
            when (val schedule = reminderModel.schedule) {
                is ReminderSchedule.AlwaysEnabled -> true
                is ReminderSchedule.DaysOfWeek -> {
                    date.dayOfWeek in schedule.daysOfWeek
                }
            }
        }
    }

    override fun readReminders(): Flow<List<ReminderModel>> =
        reminderDataSource.readReminders().map { allReminders ->
            if (allReminders.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allReminders.mapToReminderModel()
                }
            } else emptyList()
        }

    private suspend fun List<ReminderDataModel>.mapToReminderModel(): List<ReminderModel> =
        this.let { allReminders ->
            coroutineScope {
                allReminders.map {
                    async { it.toReminderModel() }
                }.awaitAll()
            }
        }

    override fun readReminderIdsByTaskId(taskId: String): Flow<List<String>> =
        reminderDataSource.readReminderIdsByTaskId(taskId)

    override fun readReminderIds(): Flow<List<String>> =
        reminderDataSource.readReminderIds()

    override suspend fun saveReminder(reminderModel: ReminderModel): ResultModel<Unit, Throwable> =
        reminderDataSource.saveReminder(reminderModel.toReminderDataModel())

    override suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable> =
        reminderDataSource.deleteReminderById(reminderId)

}