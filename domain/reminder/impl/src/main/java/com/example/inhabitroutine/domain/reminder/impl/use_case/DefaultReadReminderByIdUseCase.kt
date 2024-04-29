package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.model.reminder.ReminderModel
import com.example.inhabitroutine.domain.reminder.api.ReadReminderByIdUseCase
import kotlinx.coroutines.flow.Flow

internal class DefaultReadReminderByIdUseCase(
    private val reminderRepository: ReminderRepository
) : ReadReminderByIdUseCase {

    override operator fun invoke(reminderId: String): Flow<ReminderModel?> =
        reminderRepository.readReminderById(reminderId)

}