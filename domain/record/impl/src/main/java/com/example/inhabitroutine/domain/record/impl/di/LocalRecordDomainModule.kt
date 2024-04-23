package com.example.inhabitroutine.domain.record.impl.di

import com.example.inhabitroutine.data.record.api.RecordRepository
import com.example.inhabitroutine.domain.record.api.SaveRecordUseCase
import com.example.inhabitroutine.domain.record.impl.use_case.DefaultSaveRecordUseCase

object LocalRecordDomainModule {

    fun provideSaveRecordUseCase(
        recordRepository: RecordRepository
    ): SaveRecordUseCase {
        return DefaultSaveRecordUseCase(
            recordRepository = recordRepository
        )
    }

}