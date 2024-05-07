package com.ignatlegostaev.inhabitroutine.core.di.modules.record

import com.ignatlegostaev.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.ignatlegostaev.inhabitroutine.core.database.impl.di.LocalDatabaseModule
import com.ignatlegostaev.inhabitroutine.core.database.record.api.RecordDao
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.DefaultDispatcherQualifier
import com.ignatlegostaev.inhabitroutine.core.di.qualifiers.IODispatcherQualifier
import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.data.record.impl.data_source.RecordDataSource
import com.ignatlegostaev.inhabitroutine.data.record.impl.di.LocalRecordDataModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object RecordDataModule {

    @Provides
    fun provideRecordDao(
        db: InhabitRoutineDatabase,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher
    ): RecordDao {
        return LocalDatabaseModule.provideRecordDao(
            db = db,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    fun provideRecordDataSource(
        recordDao: RecordDao,
        json: Json,
        @IODispatcherQualifier ioDispatcher: CoroutineDispatcher
    ): RecordDataSource {
        return LocalRecordDataModule.provideRecordDataSource(
            recordDao = recordDao,
            json = json,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    fun provideRecordRepository(
        recordDataSource: RecordDataSource,
        @DefaultDispatcherQualifier defaultDispatcher: CoroutineDispatcher
    ): RecordRepository {
        return LocalRecordDataModule.provideRecordRepository(
            recordDataSource = recordDataSource,
            defaultDispatcher = defaultDispatcher
        )
    }

}