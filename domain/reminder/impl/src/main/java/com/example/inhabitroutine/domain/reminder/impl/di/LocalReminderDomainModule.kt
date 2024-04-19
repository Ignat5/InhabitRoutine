package com.example.inhabitroutine.domain.reminder.impl.di

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.impl.use_case.DefaultReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.impl.use_case.DefaultReadRemindersByTaskIdUseCase

object LocalReminderDomainModule {

    fun provideReadRemindersByTaskIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadRemindersByTaskIdUseCase {
        return DefaultReadRemindersByTaskIdUseCase(
            reminderRepository = reminderRepository
        )
    }

    fun provideReadReminderCountByTaskIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadReminderCountByTaskIdUseCase {
        return DefaultReadReminderCountByTaskIdUseCase(
            reminderRepository = reminderRepository
        )
    }
}