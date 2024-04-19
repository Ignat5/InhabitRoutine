package com.example.inhabitroutine.data.reminder.api

import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
}