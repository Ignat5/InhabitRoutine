package com.example.inhabitroutine.core.di.modules.reminder

import android.content.Context
import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ReadReminderByIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import com.example.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.example.inhabitroutine.domain.reminder.api.SetUpAllRemindersUseCase
import com.example.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.example.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.example.inhabitroutine.domain.reminder.api.UpdateReminderUseCase
import com.example.inhabitroutine.domain.reminder.impl.di.LocalReminderDomainModule
import com.example.inhabitroutine.platform.reminder.manager.DefaultReminderManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

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
    fun provideReadReminderByIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadReminderByIdUseCase {
        return LocalReminderDomainModule.provideReadReminderByIdUseCase(
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
    fun provideSetUpAllRemindersUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): SetUpAllRemindersUseCase {
        return LocalReminderDomainModule.provideSetUpAllRemindersUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideSaveReminderUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        externalScope: CoroutineScope,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): SaveReminderUseCase {
        return LocalReminderDomainModule.provideSaveReminderUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            externalScope = externalScope,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideUpdateReminderUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        externalScope: CoroutineScope,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): UpdateReminderUseCase {
        return LocalReminderDomainModule.provideUpdateReminderUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            externalScope = externalScope,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideDeleteReminderByIdUseCase(
        reminderRepository: ReminderRepository,
        reminderManager: ReminderManager
    ): DeleteReminderByIdUseCase {
        return LocalReminderDomainModule.provideDeleteReminderByIdUseCase(
            reminderRepository = reminderRepository,
            reminderManager = reminderManager
        )
    }

    @Provides
    fun provideSetUpTaskRemindersUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): SetUpTaskRemindersUseCase {
        return LocalReminderDomainModule.provideSetUpTaskRemindersUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            resetTaskRemindersUseCase = resetTaskRemindersUseCase,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideSetUpNextReminderUseCase(
        reminderManager: ReminderManager,
        reminderRepository: ReminderRepository,
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): SetUpNextReminderUseCase {
        return LocalReminderDomainModule.provideSetUpNextReminderUseCase(
            reminderManager = reminderManager,
            reminderRepository = reminderRepository,
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideResetTaskRemindersUseCase(
        reminderRepository: ReminderRepository,
        reminderManager: ReminderManager,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ResetTaskRemindersUseCase {
        return LocalReminderDomainModule.provideResetTaskRemindersUseCase(
            reminderRepository = reminderRepository,
            reminderManager = reminderManager,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideReminderManager(
        @ApplicationContext context: Context
    ): ReminderManager {
        return DefaultReminderManager(context)
    }

}