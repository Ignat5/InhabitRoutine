package com.example.inhabitroutine.core.di.modules.task

import com.example.inhabitroutine.core.database.api.db.TaskDao
import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.task.DefaultTaskDao
import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.core.di.qualifiers.IODispatcherQualifier
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.data.task.impl.repository.data_source.DefaultTaskDataSource
import com.example.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
import com.example.inhabitroutine.data.task.impl.repository.repository.DefaultTaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object TaskDataModule {

    @Provides
    fun provideTaskDataSource(
        taskDao: TaskDao,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher,
        json: Json
    ): TaskDataSource {
        return DefaultTaskDataSource(
            taskDao = taskDao,
            ioDispatcher = ioDispatcher,
            json = json
        )
    }

    @Provides
    fun provideTaskRepository(
        taskDataSource: TaskDataSource,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): TaskRepository {
        return DefaultTaskRepository(
            taskDataSource = taskDataSource,
            defaultDispatcher = defaultDispatcher
        )
    }

}