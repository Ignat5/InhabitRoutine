package com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

internal class DefaultResetTaskRemindersUseCase(
    private val reminderRepository: ReminderRepository,
    private val reminderManager: ReminderManager,
) : ResetTaskRemindersUseCase {

    override suspend operator fun invoke(taskId: String) {
        val allReminderIds = getReminderIdsByTaskId(taskId)
        allReminderIds.forEach { reminderId ->
            reminderManager.resetReminderById(reminderId)
        }
    }

    private suspend fun getReminderIdsByTaskId(taskId: String) =
        reminderRepository.readReminderIdsByTaskId(taskId).firstOrNull()
            ?: emptyList()

}