package com.example.inhabitroutine.domain.record.api

import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

interface SaveRecordUseCase {
    sealed interface RequestType {
        data class EntryNumber(val number: Double) : RequestType
        data class EntryTime(val time: LocalTime) : RequestType
        data object EntryDone : RequestType
        data object EntrySkip : RequestType
        data object EntryFail : RequestType
    }

    suspend operator fun invoke(
        taskId: String,
        date: LocalDate,
        requestType: RequestType
    ): ResultModel<Unit, Throwable>
}