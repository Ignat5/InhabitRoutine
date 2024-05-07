package com.ignatlegostaev.inhabitroutine.domain.reminder.api

import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReadRemindersByTaskIdUseCase {
    operator fun invoke(taskId: String): Flow<List<ReminderModel>>
}