package com.example.inhabitroutine.data.reminder.impl.data_source

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.reminder.impl.model.ReminderDataModel
import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderDataModel>>
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
    suspend fun saveReminder(reminderDataModel: ReminderDataModel): ResultModel<Unit, Throwable>
    suspend fun updateReminder(reminderDataModel: ReminderDataModel): ResultModel<Unit, Throwable>
    suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable>
}