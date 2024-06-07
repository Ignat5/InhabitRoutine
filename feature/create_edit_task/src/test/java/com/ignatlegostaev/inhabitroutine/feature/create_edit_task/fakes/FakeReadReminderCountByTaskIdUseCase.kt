package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.fakes

import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeReadReminderCountByTaskIdUseCase : ReadReminderCountByTaskIdUseCase {
    val reminderCountState = MutableStateFlow(0)

    override fun invoke(taskId: String): Flow<Int> {
        return reminderCountState
    }
}