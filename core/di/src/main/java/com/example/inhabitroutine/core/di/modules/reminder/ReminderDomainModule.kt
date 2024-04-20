package com.example.inhabitroutine.core.di.modules.reminder

import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.example.inhabitroutine.domain.reminder.api.UpdateReminderUseCase
import com.example.inhabitroutine.domain.reminder.impl.di.LocalReminderDomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object ReminderDomainModule {

    @Provides
    fun provideReadRemindersByTaskIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadRemindersByTaskIdUseCase {
        return LocalReminderDomainModule.provideReadRemindersByTaskIdUseCase(
            reminderRepository = reminderRepository
        )
    }

    @Provides
    fun provideReadReminderCountByTaskIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadReminderCountByTaskIdUseCase {
        return LocalReminderDomainModule.provideReadReminderCountByTaskIdUseCase(
            reminderRepository = reminderRepository
        )
    }

    @Provides
    fun provideSaveReminderUseCase(
        reminderRepository: ReminderRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): SaveReminderUseCase {
        return LocalReminderDomainModule.provideSaveReminderUseCase(
            reminderRepository = reminderRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideUpdateReminderUseCase(
        reminderRepository: ReminderRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): UpdateReminderUseCase {
        return LocalReminderDomainModule.provideUpdateReminderUseCase(
            reminderRepository = reminderRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideDeleteReminderByIdUseCase(
        reminderRepository: ReminderRepository
    ): DeleteReminderByIdUseCase {
        return LocalReminderDomainModule.provideDeleteReminderByIdUseCase(
            reminderRepository = reminderRepository
        )
    }

}