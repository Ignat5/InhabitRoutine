package com.example.inhabitroutine.core.di.modules.task

import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.domain.task.impl.di.LocalTaskDomainModule
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultReadTaskByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object TaskDomainModule {

    @Provides
    fun provideReadTaskByIdUseCase(
        taskRepository: TaskRepository
    ): ReadTaskByIdUseCase {
        return DefaultReadTaskByIdUseCase(
            taskRepository = taskRepository
        )
    }

    @Provides
    fun provideSaveTaskDraftUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): SaveTaskDraftUseCase {
        return LocalTaskDomainModule.provideSaveTaskDraftUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

}