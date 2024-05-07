package com.ignatlegostaev.inhabitroutine.core.di.components

import com.ignatlegostaev.inhabitroutine.App
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadReminderByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpAllRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

object PresentationComponent {

    private val entryPoint by lazy {
        EntryPointAccessors.fromApplication(
            App.appContext,
            PresentationEntryPoint::class.java
        )
    }

    val setUpAllRemindersUseCase: SetUpAllRemindersUseCase
        get() = entryPoint.provideSetUpAllRemindersUseCase()

    val setUpNextReminderUseCase: SetUpNextReminderUseCase
        get() = entryPoint.provideSetUpNextReminderUseCase()

    val readReminderByIdUseCase: ReadReminderByIdUseCase
        get() = entryPoint.provideReadReminderByIdUseCase()

    val readTaskByIdUseCase: ReadTaskByIdUseCase
        get() = entryPoint.provideReadTaskByIdUseCase()

    val externalScope: CoroutineScope
        get() = entryPoint.provideExternalScope()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface PresentationEntryPoint {
        fun provideSetUpAllRemindersUseCase(): SetUpAllRemindersUseCase
        fun provideSetUpNextReminderUseCase(): SetUpNextReminderUseCase
        fun provideReadReminderByIdUseCase(): ReadReminderByIdUseCase
        fun provideReadTaskByIdUseCase(): ReadTaskByIdUseCase
        fun provideExternalScope(): CoroutineScope
    }

}