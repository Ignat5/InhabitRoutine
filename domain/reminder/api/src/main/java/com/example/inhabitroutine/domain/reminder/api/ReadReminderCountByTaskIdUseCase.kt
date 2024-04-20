package com.example.inhabitroutine.domain.reminder.api

import kotlinx.coroutines.flow.Flow

interface ReadReminderCountByTaskIdUseCase {
    operator fun invoke(taskId: String): Flow<Int>
}