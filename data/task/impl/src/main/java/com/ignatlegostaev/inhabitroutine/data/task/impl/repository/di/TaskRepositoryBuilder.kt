package com.ignatlegostaev.inhabitroutine.data.task.impl.repository.di

import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.repository.DefaultTaskRepository
import kotlinx.coroutines.CoroutineDispatcher

class TaskRepositoryBuilder {
    fun build(
        taskDataSource: TaskDataSource,
        defaultDispatcher: CoroutineDispatcher
    ): TaskRepository {
        return DefaultTaskRepository(
            taskDataSource = taskDataSource,
            defaultDispatcher = defaultDispatcher
        )
    }
}