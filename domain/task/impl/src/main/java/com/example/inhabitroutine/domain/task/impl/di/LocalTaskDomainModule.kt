package com.example.inhabitroutine.domain.task.impl.di

import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.ValidateProgressLimitNumberUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultDeleteTaskByIdUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultSaveTaskDraftUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskFrequencyByIdUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskProgressByIdUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultValidateProgressLimitNumberUseCase
import kotlinx.coroutines.CoroutineDispatcher

object LocalTaskDomainModule {

    fun provideReadTaskByIdUseCase(
        taskRepository: TaskRepository,
    ): ReadTaskByIdUseCase {
        return DefaultReadTaskByIdUseCase(
            taskRepository = taskRepository
        )
    }

    fun provideSaveTaskDraftUseCase(
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): SaveTaskDraftUseCase {
        return DefaultSaveTaskDraftUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideUpdateTaskTitleByIdUseCase(
        taskRepository: TaskRepository
    ): UpdateTaskTitleByIdUseCase {
        return DefaultUpdateTaskTitleByIdUseCase(
            taskRepository = taskRepository
        )
    }

    fun provideUpdateTaskProgressByIdUseCase(
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskProgressByIdUseCase {
        return DefaultUpdateTaskProgressByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideUpdateTaskFrequencyByIdUseCase(
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): UpdateTaskFrequencyByIdUseCase {
        return DefaultUpdateTaskFrequencyByIdUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

    fun provideValidateProgressLimitNumberUseCase(): ValidateProgressLimitNumberUseCase {
        return DefaultValidateProgressLimitNumberUseCase()
    }

    fun provideDeleteTaskByIdUseCase(
        taskRepository: TaskRepository
    ): DeleteTaskByIdUseCase {
        return DefaultDeleteTaskByIdUseCase(
            taskRepository = taskRepository
        )
    }

}