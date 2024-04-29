package com.example.inhabitroutine.core.di.components

import com.example.inhabitroutine.App
import com.example.inhabitroutine.domain.reminder.api.ReadReminderByIdUseCase
import com.example.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
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
        fun provideSetUpNextReminderUseCase(): SetUpNextReminderUseCase
        fun provideReadReminderByIdUseCase(): ReadReminderByIdUseCase
        fun provideReadTaskByIdUseCase(): ReadTaskByIdUseCase
        fun provideExternalScope(): CoroutineScope
    }

}