package com.example.inhabitroutine.core.di.modules.task

import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.example.inhabitroutine.domain.task.impl.use_case.DefaultReadTaskByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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

}