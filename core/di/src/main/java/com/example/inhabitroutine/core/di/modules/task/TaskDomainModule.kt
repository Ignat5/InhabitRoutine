package com.example.inhabitroutine.core.di.modules.task

import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ValidateProgressLimitNumberUseCase
import com.example.inhabitroutine.domain.task.impl.di.LocalTaskDomainModule
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
        return LocalTaskDomainModule.provideReadTaskByIdUseCase(
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

    @Provides
    fun provideUpdateTaskTitleByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskTitleByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskTitleByIdUseCase(
            taskRepository = taskRepository
        )
    }

    @Provides
    fun provideUpdateTaskProgressByIdUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskProgressByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskProgressByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideValidateProgressLimitNumberUseCase(): ValidateProgressLimitNumberUseCase {
        return LocalTaskDomainModule.provideValidateProgressLimitNumberUseCase()
    }

}