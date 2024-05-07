package com.ignatlegostaev.inhabitroutine.domain.reminder.api

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel

interface UpdateReminderUseCase {
    sealed interface UpdateReminderFailure {
        data object Overlap : UpdateReminderFailure
        data class Other(val throwable: Throwable) : UpdateReminderFailure
    }
    suspend operator fun invoke(reminderModel: ReminderModel): ResultModel<Unit, UpdateReminderFailure>
}