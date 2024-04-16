package com.example.inhabitroutine.domain.task.impl.di

import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultSaveTaskDraftUseCase
import kotlinx.coroutines.CoroutineDispatcher

object LocalTaskDomainModule {

    fun provideSaveTaskDraftUseCase(
        taskRepository: TaskRepository,
        defaultDispatcher: CoroutineDispatcher
    ): SaveTaskDraftUseCase {
        return DefaultSaveTaskDraftUseCase(
            taskRepository = taskRepository,
            defaultDispatcher = defaultDispatcher
        )
    }

}