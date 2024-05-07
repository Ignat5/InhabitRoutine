package com.ignatlegostaev.inhabitroutine.domain.reminder.impl.di

import com.ignatlegostaev.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadReminderByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpAllRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.UpdateReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultDeleteReminderByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultReadReminderByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultReadReminderCountByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultReadRemindersByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultResetTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultSaveReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultSetUpAllRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultSetUpNextReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultSetUpTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultUpdateReminderUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

object LocalReminderDomainModule {

    fun provideReadRemindersByTaskIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadRemindersByTaskIdUseCase {
        return DefaultReadRemindersByTaskIdUseCase(
            reminderRepository = reminderRepository
        )
    }

    fun provideReadReminderByIdUseCase(
        reminderRepository: ReminderRepository
    ): ReadReminderByIdUseCase {
        return DefaultReadReminderByIdUseCase(
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

    fun provideSetUpAllRemindersUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        defaultDispatcher: CoroutineDispatcher
    ): SetUpAllRemindersUseCase {
        return DefaultSetUpAllRemindersUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideSaveReminderUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        externalScope: CoroutineScope,
        defaultDispatcher: CoroutineDispatcher
    ): SaveReminderUseCase {
        return DefaultSaveReminderUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            externalScope = externalScope,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideUpdateReminderUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        externalScope: CoroutineScope,
        defaultDispatcher: CoroutineDispatcher
    ): UpdateReminderUseCase {
        return DefaultUpdateReminderUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            externalScope = externalScope,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideDeleteReminderByIdUseCase(
        reminderRepository: ReminderRepository,
        reminderManager: ReminderManager
    ): DeleteReminderByIdUseCase {
        return DefaultDeleteReminderByIdUseCase(
            reminderRepository = reminderRepository,
            reminderManager = reminderManager
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

    fun provideSetUpTaskRemindersUseCase(
        reminderRepository: ReminderRepository,
        setUpNextReminderUseCase: SetUpNextReminderUseCase,
        resetTaskRemindersUseCase: ResetTaskRemindersUseCase,
        defaultDispatcher: CoroutineDispatcher
    ): SetUpTaskRemindersUseCase {
        return DefaultSetUpTaskRemindersUseCase(
            reminderRepository = reminderRepository,
            setUpNextReminderUseCase = setUpNextReminderUseCase,
            resetTaskRemindersUseCase = resetTaskRemindersUseCase,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideResetTaskRemindersUseCase(
        reminderRepository: ReminderRepository,
        reminderManager: ReminderManager,
        defaultDispatcher: CoroutineDispatcher
    ): ResetTaskRemindersUseCase {
        return DefaultResetTaskRemindersUseCase(
            reminderRepository = reminderRepository,
            reminderManager = reminderManager,
            defaultDispatcher = defaultDispatcher
        )
    }

}