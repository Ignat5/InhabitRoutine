package com.ignatlegostaev.inhabitroutine.data.task.impl.repository.di

import com.ignatlegostaev.inhabitroutine.core.database.task.api.TaskDao
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.data_source.DefaultTaskDataSource
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
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