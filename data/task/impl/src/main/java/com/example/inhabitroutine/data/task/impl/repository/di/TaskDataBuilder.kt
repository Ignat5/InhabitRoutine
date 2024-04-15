package com.example.inhabitroutine.data.task.impl.repository.di

import com.example.inhabitroutine.core.database.task.api.TaskDao
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.data.task.impl.repository.data_source.DefaultTaskDataSource
import com.example.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
import com.example.inhabitroutine.data.task.impl.repository.repository.DefaultTaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

object TaskDataBuilder {
    fun buildTaskDataSource(
        taskDao: TaskDao,
        ioDispatcher: CoroutineDispatcher,
        json: Json
    ): TaskDataSource {
        return DefaultTaskDataSource(
            taskDao = taskDao,
            ioDispatcher = ioDispatcher,
            json = json
        )
    }
    fun buildTaskRepository(
        taskDataSource: TaskDataSource,
        defaultDispatcher: CoroutineDispatcher
    ): TaskRepository {
        return DefaultTaskRepository(
            taskDataSource = taskDataSource,
            defaultDispatcher = defaultDispatcher
        )
    }
}