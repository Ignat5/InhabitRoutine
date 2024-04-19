package com.example.inhabitroutine.core.database.reminder.api

import kotlinx.coroutines.flow.Flow

interface ReminderDao {
    fun readRemindersByTaskId(taskId: String): Flow<List<ReminderEntity>>
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
}