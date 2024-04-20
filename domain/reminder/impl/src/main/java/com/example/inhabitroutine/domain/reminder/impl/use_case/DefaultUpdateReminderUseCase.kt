package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.reminder.api.UpdateReminderUseCase

internal class DefaultUpdateReminderUseCase(
    private val reminderRepository: ReminderRepository
) : UpdateReminderUseCase {

    override suspend operator fun invoke(reminderModel: ReminderModel): ResultModel<Unit, Throwable> {
        val resultModel = reminderRepository.updateReminder(reminderModel)
        if (resultModel is ResultModel.Success) {
//            TODO("set up reminder")
        }
        return resultModel
    }

}