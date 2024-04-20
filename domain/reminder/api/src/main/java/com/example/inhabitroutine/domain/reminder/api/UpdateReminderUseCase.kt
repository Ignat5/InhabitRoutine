package com.example.inhabitroutine.domain.reminder.api

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.reminder.ReminderModel

interface UpdateReminderUseCase {
    suspend operator fun invoke(reminderModel: ReminderModel): ResultModel<Unit, Throwable>
}