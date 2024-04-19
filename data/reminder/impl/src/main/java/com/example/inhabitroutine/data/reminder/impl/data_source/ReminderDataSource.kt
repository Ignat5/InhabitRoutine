package com.example.inhabitroutine.data.reminder.impl.data_source

import kotlinx.coroutines.flow.Flow

interface ReminderDataSource {
    fun readReminderCountByTaskId(taskId: String): Flow<Int>
}