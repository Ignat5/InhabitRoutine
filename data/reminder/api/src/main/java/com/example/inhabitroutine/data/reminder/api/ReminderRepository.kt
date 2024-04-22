package com.example.inhabitroutine.data.reminder.api

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderModel>>
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
    fun readReminders(): Flow<List<ReminderModel>>
    suspend fun saveReminder(reminderModel: ReminderModel): ResultModel<Unit, Throwable>
    suspend fun updateReminder(reminderModel: ReminderModel): ResultModel<Unit, Throwable>
    suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable>
}