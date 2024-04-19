package com.example.inhabitroutine.domain.reminder.impl.di

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.impl.use_case.DefaultReadReminderCountByTaskIdUseCase

object LocalReminderDomainModule {
    fun provideReadReminderCountByTaskIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadReminderCountByTaskIdUseCase {
        return DefaultReadReminderCountByTaskIdUseCase(
            reminderRepository = reminderRepository
        )
    }
}