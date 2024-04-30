package com.example.inhabitroutine.core.database.record.api

import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.flow.Flow

interface RecordDao {
    fun readRecordsByDate(targetEpochDay: Long): Flow<List<RecordEntity>>
    fun readRecordsByTaskId(taskId: String): Flow<List<RecordEntity>>
    suspend fun saveRecord(recordEntity: RecordEntity): ResultModel<Unit, Throwable>
    suspend fun deleteRecordByTaskIdAndDate(taskId: String, targetEpochDay: Long): ResultModel<Unit, Throwable>
    suspend fun deleteRecordsByTaskIdAndPeriod(
        taskId: String,
        minEpochDay: Long,
        maxEpochDay: Long
    ): ResultModel<Unit, Throwable>
    suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit, Throwable>
}