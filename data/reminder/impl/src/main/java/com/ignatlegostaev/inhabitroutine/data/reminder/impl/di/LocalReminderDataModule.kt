package com.ignatlegostaev.inhabitroutine.data.reminder.impl.di

import com.ignatlegostaev.inhabitroutine.core.database.reminder.api.ReminderDao
import com.ignatlegostaev.inhabitroutine.data.reminder.api.ReminderRepository
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.data_source.DefaultReminderDataSource
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.data_source.ReminderDataSource
import com.ignatlegostaev.inhabitroutine.data.reminder.impl.repository.DefaultReminderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

object LocalReminderDataModule {
    fun provideReminderDataSource(
        reminderDao: ReminderDao,
        ioDispatcher: CoroutineDispatcher,
        json: Json
    ): ReminderDataSource {
        return DefaultReminderDataSource(
            reminderDao = reminderDao,
            ioDispatcher = ioDispatcher,
            json = json
        )
    }

    fun provideReminderRepository(
        reminderDataSource: ReminderDataSource,
        defaultDispatcher: CoroutineDispatcher
    ): ReminderRepository {
        return DefaultReminderRepository(
            reminderDataSource = reminderDataSource,
            defaultDispatcher = defaultDispatcher
        )
    }
}