package com.example.inhabitroutine.core.di.modules.core

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.inhabitroutine.core.database.InhabitRoutineDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "InhabitRoutineDatabase"

@Module
@InstallIn(SingletonComponent::class)
object CoreDataModule {

    @Provides
    @Singleton
    fun provideSqlDriver(
        @ApplicationContext context: Context
    ): SqlDriver {
        return AndroidSqliteDriver(
            schema = InhabitRoutineDatabase.Schema,
            context = context,
            name = DATABASE_NAME,
            callback = object  : AndroidSqliteDriver.Callback(InhabitRoutineDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(
        sqlDriver: SqlDriver
    ): InhabitRoutineDatabase {
        return InhabitRoutineDatabase.invoke(
            driver = sqlDriver
        )
    }

}