package com.ignatlegostaev.inhabitroutine.core.database.reminder.api

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.flow.Flow

interface ReminderDao {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderEntity>>
    fun readReminderById(reminderId: String): Flow<ReminderEntity?>
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
    fun readRemindersByDate(targetEpochDay: Long): Flow<List<ReminderEntity>>
    fun readReminders(): Flow<List<ReminderEntity>>
    fun readReminderIdsByTaskId(taskId: String): Flow<List<String>>
    fun readReminderIds(): Flow<List<String>>
    suspend fun saveReminder(reminderEntity: ReminderEntity): ResultModel<Unit, Throwable>
    suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable>
}