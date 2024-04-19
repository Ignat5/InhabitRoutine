package com.example.inhabitroutine.data.reminder.api

import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderModel>>
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
}