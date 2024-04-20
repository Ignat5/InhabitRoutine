package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase

internal class DefaultDeleteReminderByIdUseCase(
    private val reminderRepository: ReminderRepository
) : DeleteReminderByIdUseCase {

    override suspend operator fun invoke(reminderId: String): ResultModel<Unit, Throwable> =
        reminderRepository.deleteReminderById(reminderId)

}