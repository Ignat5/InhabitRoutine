package com.ignatlegostaev.inhabitroutine.domain.reminder.api

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import kotlinx.datetime.LocalTime

interface SaveReminderUseCase {

    sealed interface SaveReminderFailure {
        data object Overlap : SaveReminderFailure
        data class Other(val throwable: Throwable) : SaveReminderFailure
    }

    suspend operator fun invoke(
        taskId: String,
        time: LocalTime,
        type: ReminderType,
        schedule: ReminderSchedule
    ): ResultModel<String, SaveReminderFailure>
}