package com.ignatlegostaev.inhabitroutine.data.reminder.impl.data_source

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.model.ReminderDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ReminderDataSource {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderDataModel>>
    fun readReminderById(reminderId: String): Flow<ReminderDataModel?>
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
    fun readRemindersByDate(targetDate: LocalDate): Flow<List<ReminderDataModel>>
    fun readReminders(): Flow<List<ReminderDataModel>>
    fun readReminderIdsByTaskId(taskId: String): Flow<List<String>>
    fun readReminderIds(): Flow<List<String>>
    suspend fun saveReminder(reminderDataModel: ReminderDataModel): ResultModel<Unit, Throwable>
    suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable>
}