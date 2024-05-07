package com.ignatlegostaev.inhabitroutine.core.di.modules.task

import com.ignatlegostaev.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.ignatlegostaev.inhabitroutine.core.database.impl.di.LocalDatabaseModule
import com.ignatlegostaev.inhabitroutine.core.database.task.api.TaskDao
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.IODispatcherQualifier
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.di.TaskDataSourceBuilder
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.di.TaskRepositoryBuilder
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
        return LocalDatabaseModule.provideTaskDao(
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
        return TaskDataSourceBuilder().build(
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
        return TaskRepositoryBuilder().build(
            taskDataSource = taskDataSource,
            defaultDispatcher = defaultDispatcher
        )
    }

}