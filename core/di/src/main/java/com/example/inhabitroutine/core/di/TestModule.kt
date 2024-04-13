package com.example.inhabitroutine.core.di

//import android.content.Context
//import app.cash.sqldelight.db.SqlDriver
//import app.cash.sqldelight.driver.android.AndroidSqliteDriver
//import com.example.inhabitroutine.core.database.InhabitRoutineDatabase
//import com.example.inhabitroutine.data.task.api.TaskRepository
//import com.example.inhabitroutine.data.task.impl.repository.data_source.DefaultTaskDataSource
//import com.example.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
//import com.example.inhabitroutine.data.task.impl.repository.repository.DefaultTaskRepository
//import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
//import com.example.inhabitroutine.domain.task.impl.use_case.DefaultReadTaskByIdUseCase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import kotlinx.coroutines.Dispatchers
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class TestModule {
//
//    @Provides
//    fun provideTestString(
//        @ApplicationContext context: Context
//    ): String {
//        return "test string here"
//    }
//
//    @Provides
//    @Singleton
//    fun provideSqlDriver(
//        @ApplicationContext context: Context
//    ): SqlDriver {
//        return AndroidSqliteDriver(
//            context = context,
//            schema = InhabitRoutineDatabase.Schema
//        )
//    }
//
//    @Provides
//    @Singleton
//    fun provideDatabase(
//        sqlDriver: SqlDriver
//    ): InhabitRoutineDatabase {
//        return InhabitRoutineDatabase.invoke(sqlDriver)
//    }
//
//    @Provides
//    @Singleton
//    fun provideTaskDataSource(
//        db: InhabitRoutineDatabase
//    ): TaskDataSource {
//        return DefaultTaskDataSource(
////            db = db,
////            Dispatchers.IO,
////            json = TODO()
//        )
//    }
//
//    @Provides
//    @Singleton
//    fun provideTaskRepository(
//        taskDataSource: TaskDataSource
//    ): TaskRepository {
//        return DefaultTaskRepository(
//            taskDataSource = taskDataSource
//        )
//    }
//
//    @Provides
//    @Singleton
//    fun provideReadTaskByIdUseCase(
//        taskRepository: TaskRepository
//    ): ReadTaskByIdUseCase {
//        return DefaultReadTaskByIdUseCase(
//            taskRepository = taskRepository
//        )
//    }
//
//}