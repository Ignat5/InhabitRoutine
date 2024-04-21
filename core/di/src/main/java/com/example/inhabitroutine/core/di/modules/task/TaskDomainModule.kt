package com.example.inhabitroutine.core.di.modules.task

import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ReadTasksByQueryUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ValidateProgressLimitNumberUseCase
import com.example.inhabitroutine.domain.task.impl.di.LocalTaskDomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

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
    fun provideReadTasksByQueryUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ReadTasksByQueryUseCase {
        return LocalTaskDomainModule.provideReadTasksByQuery(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
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
    fun provideUpdateTaskDateByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskDateByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskDateByIdUseCase(
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
    fun provideUpdateTaskFrequencyByIdUseCase(
        taskRepository: TaskRepository,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskFrequencyByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskFrequencyByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideUpdateTaskDescriptionByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskDescriptionByIdUseCase {
        return LocalTaskDomainModule.provideUpdateTaskDescriptionByIdUseCase(
            taskRepository = taskRepository
        )
    }

    @Provides
    fun provideSaveTaskByIdUseCase(
        taskRepository: TaskRepository,
        externalScope: CoroutineScope
    ): SaveTaskByIdUseCase {
        return LocalTaskDomainModule.provideSaveTaskByIdUseCase(
            taskRepository = taskRepository,
            externalScope = externalScope
        )
    }

    @Provides
    fun provideArchiveTaskByIdUseCase(
        taskRepository: TaskRepository,
        externalScope: CoroutineScope,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): ArchiveTaskByIdUseCase {
        return LocalTaskDomainModule.provideArchiveTaskByIdUseCase(
            taskRepository = taskRepository,
            externalScope = externalScope,
            defaultDispatcher = defaultDispatcher
        )
    }

    @Provides
    fun provideValidateProgressLimitNumberUseCase(): ValidateProgressLimitNumberUseCase {
        return LocalTaskDomainModule.provideValidateProgressLimitNumberUseCase()
    }

    @Provides
    fun provideDeleteTaskByIdUseCase(
        taskRepository: TaskRepository
    ): DeleteTaskByIdUseCase {
        return LocalTaskDomainModule.provideDeleteTaskByIdUseCase(
            taskRepository = taskRepository
        )
    }

}