package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase

internal class DefaultDeleteReminderByIdUseCase(
    private val reminderRepository: ReminderRepository,
    private val reminderManager: ReminderManager
) : DeleteReminderByIdUseCase {

    override suspend operator fun invoke(reminderId: String): ResultModel<Unit, Throwable> {
        val resultModel = reminderRepository.deleteReminderById(reminderId)
        if (resultModel is ResultModel.Success) {
            reminderManager.resetReminderById(reminderId)
        }
        return resultModel
    }

}