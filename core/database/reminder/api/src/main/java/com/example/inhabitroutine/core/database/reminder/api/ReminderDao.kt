package com.example.inhabitroutine.core.database.reminder.api

import kotlinx.coroutines.flow.Flow

interface ReminderDao {
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
}