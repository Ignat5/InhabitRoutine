package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import kotlinx.coroutines.flow.Flow

internal class DefaultReadReminderCountByTaskIdUseCase(
    private val reminderRepository: ReminderRepository
) : ReadReminderCountByTaskIdUseCase {

    override operator fun invoke(taskId: String): Flow<Int> =
        reminderRepository.readReminderCountByTaskId(taskId)

}