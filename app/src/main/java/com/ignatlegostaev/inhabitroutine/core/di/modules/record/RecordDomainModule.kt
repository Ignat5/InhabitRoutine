package com.ignatlegostaev.inhabitroutine.core.di.modules.record

import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.domain.record.api.DeleteRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.record.api.SaveRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.record.impl.di.LocalRecordDomainModule
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

    @Provides
    fun provideDeleteRecordUseCase(
        recordRepository: RecordRepository
    ): DeleteRecordUseCase {
        return LocalRecordDomainModule.provideDeleteRecordUseCase(
            recordRepository = recordRepository
        )
    }

}