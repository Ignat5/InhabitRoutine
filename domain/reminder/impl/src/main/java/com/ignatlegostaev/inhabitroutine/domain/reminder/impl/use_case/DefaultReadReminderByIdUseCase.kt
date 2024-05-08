package com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case

import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadReminderByIdUseCase
import kotlinx.coroutines.flow.Flow

internal class DefaultReadReminderByIdUseCase(
    private val reminderRepository: ReminderRepository
) : ReadReminderByIdUseCase {

    override operator fun invoke(reminderId: String): Flow<ReminderModel?> =
        reminderRepository.readReminderById(reminderId)

}