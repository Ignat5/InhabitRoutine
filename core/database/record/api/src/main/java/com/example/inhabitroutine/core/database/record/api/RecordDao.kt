package com.example.inhabitroutine.core.database.record.api

import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.flow.Flow

interface RecordDao {
    fun readRecordsByDate(targetEpochDay: Long): Flow<List<RecordEntity>>
    suspend fun saveRecord(recordEntity: RecordEntity): ResultModel<Unit, Throwable>
}