package com.example.inhabitroutine.data.record.api

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.domain.model.record.RecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface RecordRepository {
    fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordModel>>
    suspend fun saveRecord(recordModel: RecordModel): ResultModel<Unit, Throwable>
}