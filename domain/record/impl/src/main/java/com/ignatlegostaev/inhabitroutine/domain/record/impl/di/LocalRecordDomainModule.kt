package com.ignatlegostaev.inhabitroutine.domain.record.impl.di

import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.domain.record.api.DeleteRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.record.api.SaveRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.record.impl.use_case.DefaultDeleteRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.record.impl.use_case.DefaultSaveRecordUseCase

object LocalRecordDomainModule {

    fun provideSaveRecordUseCase(
        recordRepository: RecordRepository
    ): SaveRecordUseCase {
        return DefaultSaveRecordUseCase(
            recordRepository = recordRepository
        )
    }

    fun provideDeleteRecordUseCase(
        recordRepository: RecordRepository
    ): DeleteRecordUseCase {
        return DefaultDeleteRecordUseCase(
            recordRepository = recordRepository
        )
    }

}