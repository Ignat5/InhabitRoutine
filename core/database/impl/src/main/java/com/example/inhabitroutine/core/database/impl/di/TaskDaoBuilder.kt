package com.example.inhabitroutine.core.database.impl.di

import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.task.DefaultTaskDao
import com.example.inhabitroutine.core.database.task.api.TaskDao
import kotlinx.coroutines.CoroutineDispatcher

class TaskDaoBuilder {
    fun build(
        db: InhabitRoutineDatabase,
        ioDispatcher: CoroutineDispatcher
    ): TaskDao {
        return DefaultTaskDao(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }
}