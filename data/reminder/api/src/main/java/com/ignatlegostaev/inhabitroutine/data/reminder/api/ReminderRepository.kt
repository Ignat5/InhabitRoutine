package com.ignatlegostaev.inhabitroutine.data.reminder.api

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ReminderRepository {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderModel>>
    fun readReminderById(reminderId: String): Flow<ReminderModel?>
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
    fun readRemindersByDate(targetDate: LocalDate): Flow<List<ReminderModel>>
    fun readReminders(): Flow<List<ReminderModel>>
    fun readReminderIdsByTaskId(taskId: String): Flow<List<String>>
    fun readReminderIds(): Flow<List<String>>
    suspend fun saveReminder(reminderModel: ReminderModel): ResultModel<Unit, Throwable>
    suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable>
}