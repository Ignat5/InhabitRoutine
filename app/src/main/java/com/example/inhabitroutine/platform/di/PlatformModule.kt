package com.example.inhabitroutine.platform.di

import android.content.Context
import com.example.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.example.inhabitroutine.platform.reminder.manager.DefaultReminderManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PlatformModule {

    @Provides
    fun provideReminderManager(
        @ApplicationContext context: Context
    ): ReminderManager {
        return DefaultReminderManager(context)
    }

}