package com.example.inhabitroutine.data.record.impl.data_source

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.record.impl.model.RecordDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface RecordDataSource {
    fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordDataModel>>
    suspend fun saveRecord(recordDataModel: RecordDataModel): ResultModel<Unit, Throwable>
}