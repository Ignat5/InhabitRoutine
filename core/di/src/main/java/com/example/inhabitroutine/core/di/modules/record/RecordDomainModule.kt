package com.example.inhabitroutine.core.di.modules.record

import com.example.inhabitroutine.data.record.api.RecordRepository
import com.example.inhabitroutine.domain.record.api.SaveRecordUseCase
import com.example.inhabitroutine.domain.record.impl.di.LocalRecordDomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RecordDomainModule {

    @Provides
    fun provideSaveRecordUseCase(
        recordRepository: RecordRepository
    ): SaveRecordUseCase {
        return LocalRecordDomainModule.provideSaveRecordUseCase(
            recordRepository = recordRepository
        )
    }

}