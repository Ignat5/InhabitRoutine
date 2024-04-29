package com.example.inhabitroutine.domain.reminder.impl.di

import com.example.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.example.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.example.inhabitroutine.domain.reminder.api.UpdateReminderUseCase
import com.example.inhabitroutine.domain.reminder.impl.use_case.DefaultDeleteReminderByIdUseCase
import com.example.inhabitroutine.domain.reminder.impl.use_case.DefaultReadReminderCountByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.impl.use_case.DefaultReadRemindersByTaskIdUseCase
import com.example.inhabitroutine.domain.reminder.impl.use_case.DefaultSaveReminderUseCase
import com.example.inhabitroutine.domain.reminder.impl.use_case.DefaultSetUpNextReminderUseCase
import com.example.inhabitroutine.domain.reminder.impl.use_case.DefaultUpdateReminderUseCase
import kotlinx.coroutines.CoroutineDispatcher

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

    fun provideSaveReminderUseCase(
        reminderRepository: ReminderRepository,
        defaultDispatcher: CoroutineDispatcher
    ): SaveReminderUseCase {
        return DefaultSaveReminderUseCase(
            reminderRepository = reminderRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideUpdateReminderUseCase(
        reminderRepository: ReminderRepository,
        defaultDispatcher: CoroutineDispatcher
    ): UpdateReminderUseCase {
        return DefaultUpdateReminderUseCase(
            reminderRepository = reminderRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideDeleteReminderByIdUseCase(
        reminderRepository: ReminderRepository
    ): DeleteReminderByIdUseCase {
        return DefaultDeleteReminderByIdUseCase(
            reminderRepository = reminderRepository
        )
    }

    fun provideSetUpNextReminderUseCase(
        reminderManager: ReminderManager,
        reminderRepository: ReminderRepository,
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): SetUpNextReminderUseCase {
        return DefaultSetUpNextReminderUseCase(
            reminderManager = reminderManager,
            reminderRepository = reminderRepository,
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

}