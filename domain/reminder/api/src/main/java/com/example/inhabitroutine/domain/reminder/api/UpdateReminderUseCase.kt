package com.example.inhabitroutine.domain.reminder.api

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.reminder.ReminderModel

interface UpdateReminderUseCase {
    sealed interface UpdateReminderFailure {
        data object Overlap : UpdateReminderFailure
        data class Other(val throwable: Throwable) : UpdateReminderFailure
    }
    suspend operator fun invoke(reminderModel: ReminderModel): ResultModel<Unit, UpdateReminderFailure>
}