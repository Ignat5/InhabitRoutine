package com.example.inhabitroutine.data.task.impl.repository.di

import com.example.inhabitroutine.core.database.task.api.TaskDao
import com.example.inhabitroutine.data.task.impl.repository.data_source.DefaultTaskDataSource
import com.example.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

class TaskDataSourceBuilder {
    fun build(
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
}