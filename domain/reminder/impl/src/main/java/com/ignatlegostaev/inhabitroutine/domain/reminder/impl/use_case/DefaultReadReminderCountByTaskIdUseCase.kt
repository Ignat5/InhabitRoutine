package com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case

import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import kotlinx.coroutines.flow.Flow

internal class DefaultReadReminderCountByTaskIdUseCase(
    private val reminderRepository: ReminderRepository
) : ReadReminderCountByTaskIdUseCase {

    override operator fun invoke(taskId: String): Flow<Int> =
        reminderRepository.readReminderCountByTaskId(taskId)

}