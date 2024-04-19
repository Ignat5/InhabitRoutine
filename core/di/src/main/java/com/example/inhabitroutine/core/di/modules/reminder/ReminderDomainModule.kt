package com.example.inhabitroutine.core.di.modules.reminder

import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.impl.di.LocalReminderDomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ReminderDomainModule {

    @Provides
    fun provideReadReminderCountByTaskIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadReminderCountByTaskIdUseCase {
        return LocalReminderDomainModule.provideReadReminderCountByTaskIdUseCase(
            reminderRepository = reminderRepository
        )
    }

}