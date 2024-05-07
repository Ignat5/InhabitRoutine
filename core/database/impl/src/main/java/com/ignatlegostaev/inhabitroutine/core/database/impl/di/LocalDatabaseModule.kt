package com.ignatlegostaev.inhabitroutine.core.database.impl.di

import com.ignatlegostaev.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.ignatlegostaev.inhabitroutine.core.database.impl.record.DefaultRecordDao
import com.ignatlegostaev.inhabitroutine.core.database.impl.reminder.DefaultReminderDao
import com.ignatlegostaev.inhabitroutine.core.database.impl.task.DefaultTaskDao
import com.ignatlegostaev.inhabitroutine.core.database.record.api.RecordDao
import com.ignatlegostaev.inhabitroutine.core.database.reminder.api.ReminderDao
import com.ignatlegostaev.inhabitroutine.core.database.task.api.TaskDao
import kotlinx.coroutines.CoroutineDispatcher

object LocalDatabaseModule {
    fun provideTaskDao(
        db: InhabitRoutineDatabase,
        ioDispatcher: CoroutineDispatcher
    ): TaskDao {
        return DefaultTaskDao(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    fun provideReminderDao(
        db: InhabitRoutineDatabase,
        ioDispatcher: CoroutineDispatcher
    ): ReminderDao {
        return DefaultReminderDao(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    fun provideRecordDao(
        db: InhabitRoutineDatabase,
        ioDispatcher: CoroutineDispatcher
    ): RecordDao {
        return DefaultRecordDao(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }
}