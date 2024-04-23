package com.example.inhabitroutine.data.record.impl.di

import com.example.inhabitroutine.core.database.record.api.RecordDao
import com.example.inhabitroutine.data.record.api.RecordRepository
import com.example.inhabitroutine.data.record.impl.data_source.DefaultRecordDataSource
import com.example.inhabitroutine.data.record.impl.data_source.RecordDataSource
import com.example.inhabitroutine.data.record.impl.repository.DefaultRecordRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json

object LocalRecordDataModule {

    fun provideRecordDataSource(
        recordDao: RecordDao,
        json: Json,
        ioDispatcher: CoroutineDispatcher
    ): RecordDataSource {
        return DefaultRecordDataSource(
            recordDao = recordDao,
            json = json,
            ioDispatcher = ioDispatcher
        )
    }

    fun provideRecordRepository(
        recordDataSource: RecordDataSource,
        defaultDispatcher: CoroutineDispatcher
    ): RecordRepository {
        return DefaultRecordRepository(
            recordDataSource = recordDataSource,
            defaultDispatcher = defaultDispatcher
        )
    }

}