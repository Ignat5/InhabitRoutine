package com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case

import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import kotlinx.coroutines.flow.Flow

internal class DefaultReadRemindersByTaskIdUseCase(
    private val reminderRepository: ReminderRepository
) : ReadRemindersByTaskIdUseCase {

    override operator fun invoke(taskId: String): Flow<List<ReminderModel>> =
        reminderRepository.readRemindersByTaskId(taskId)

}