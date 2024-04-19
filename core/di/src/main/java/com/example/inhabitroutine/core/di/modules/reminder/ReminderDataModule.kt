package com.example.inhabitroutine.core.di.modules.reminder

import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.di.LocalDatabaseModule
import com.example.inhabitroutine.core.database.reminder.api.ReminderDao
import com.example.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.example.inhabitroutine.core.di.qualifiers.IODispatcherQualifier
import com.example.inhabitroutine.data.reminder.api.ReminderRepository
import com.example.inhabitroutine.data.reminder.impl.data_source.ReminderDataSource
import com.example.inhabitroutine.data.reminder.impl.di.LocalReminderDataModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object ReminderDataModule {

    @Provides
    fun provideReminderDao(
        db: InhabitRoutineDatabase,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher
    ): ReminderDao {
        return LocalDatabaseModule.provideReminderDao(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    fun provideReminderDataSource(
        reminderDao: ReminderDao,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher,
        json: Json
    ): ReminderDataSource {
        return LocalReminderDataModule.provideReminderDataSource(
            reminderDao = reminderDao,
            ioDispatcher = ioDispatcher,
            json = json
        )
    }

    @Provides
    fun provideReminderRepository(
        reminderDataSource: ReminderDataSource,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher,
    ): ReminderRepository {
        return LocalReminderDataModule.provideReminderRepository(
            reminderDataSource = reminderDataSource,
            defaultDispatcher = defaultDispatcher
        )
    }

}