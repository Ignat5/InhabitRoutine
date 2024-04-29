package com.example.inhabitroutine.domain.reminder.impl.use_case

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.SetUpAllRemindersUseCase
import com.example.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DefaultSetUpAllRemindersUseCase(
    private val reminderRepository: ReminderRepository,
    private val setUpNextReminderUseCase: SetUpNextReminderUseCase,
    private val defaultDispatcher: CoroutineDispatcher
) : SetUpAllRemindersUseCase {

    override suspend operator fun invoke() {
        withContext(defaultDispatcher) {
            reminderRepository.readReminderIds().firstOrNull()?.let { reminderIds ->
                reminderIds.forEach { reminderId ->
                    launch {
                        setUpNextReminderUseCase(reminderId)
                    }
                }
            }
        }
    }

}