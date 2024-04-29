package com.example.inhabitroutine.core.database.reminder.api

import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.flow.Flow

interface ReminderDao {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderEntity>>
    fun readReminderById(reminderId: String): Flow<ReminderEntity?>
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
    fun readRemindersByDate(targetEpochDay: Long): Flow<List<ReminderEntity>>
    fun readReminders(): Flow<List<ReminderEntity>>
    suspend fun saveReminder(reminderEntity: ReminderEntity): ResultModel<Unit, Throwable>
    suspend fun updateReminder(reminderEntity: ReminderEntity): ResultModel<Unit, Throwable>
    suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable>
}