package com.example.inhabitroutine.data.reminder.impl.data_source

import com.example.inhabitroutine.data.reminder.impl.model.ReminderDataModel
import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderDataModel>>
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
}