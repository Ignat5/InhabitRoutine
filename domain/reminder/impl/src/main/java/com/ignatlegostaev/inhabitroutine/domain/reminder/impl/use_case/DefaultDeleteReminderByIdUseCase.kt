package com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase

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