package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

internal class DefaultResetTaskRemindersUseCase(
    private val reminderRepository: ReminderRepository,
    private val reminderManager: ReminderManager,
    private val defaultDispatcher: CoroutineDispatcher
) : ResetTaskRemindersUseCase {

    override suspend operator fun invoke(taskId: String) {
        withContext(defaultDispatcher) {
            reminderRepository.readReminderIdsByTaskId(taskId).firstOrNull()?.toSet()
                ?.let { allReminderIds ->
                    allReminderIds.forEach { reminderId ->
                        reminderManager.resetReminderById(reminderId)
                    }
                }
        }
    }

}