package com.example.inhabitroutine.domain.task.impl.di

import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultSaveTaskDraftUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskTitleByIdUseCase
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

}