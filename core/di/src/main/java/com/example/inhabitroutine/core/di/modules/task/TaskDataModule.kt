package com.example.inhabitroutine.core.di.modules.task

import com.example.inhabitroutine.core.database.impl.di.DaoBuilder
import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.task.api.TaskDao
import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.core.di.qualifiers.IODispatcherQualifier
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.data.task.impl.repository.di.TaskDataBuilder
import com.example.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
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
    fun provideTaskDao(
        db: InhabitRoutineDatabase,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher
    ): TaskDao {
        return DaoBuilder.buildTaskDao(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    fun provideTaskDataSource(
        taskDao: TaskDao,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher,
        json: Json
    ): TaskDataSource {
        return TaskDataBuilder.buildTaskDataSource(
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
        return TaskDataBuilder.buildTaskRepository(
            taskDataSource = taskDataSource,
            defaultDispatcher = defaultDispatcher
        )
    }

}