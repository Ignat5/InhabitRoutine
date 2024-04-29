package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import com.example.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.example.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DefaultSetUpTaskRemindersUseCase(
    private val reminderRepository: ReminderRepository,
    private val setUpNextReminderUseCase: SetUpNextReminderUseCase,
    private val resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : SetUpTaskRemindersUseCase {

    override suspend operator fun invoke(taskId: String) {
        withContext(defaultDispatcher) {
            val resetDef = async { resetTaskRemindersUseCase(taskId) }
            reminderRepository.readReminderIdsByTaskId(taskId).firstOrNull()?.toSet()
                ?.let { allReminderIds ->
                    resetDef.await()
                    allReminderIds.forEach { reminderId ->
                        launch {
                            setUpNextReminderUseCase(reminderId)
                        }
                    }
                }
        }
    }

}