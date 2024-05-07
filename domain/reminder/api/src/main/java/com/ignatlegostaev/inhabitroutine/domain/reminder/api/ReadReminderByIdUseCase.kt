package com.ignatlegostaev.inhabitroutine.domain.reminder.api

import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import kotlinx.coroutines.flow.Flow

interface ReadReminderByIdUseCase {
    operator fun invoke(reminderId: String): Flow<ReminderModel?>
}