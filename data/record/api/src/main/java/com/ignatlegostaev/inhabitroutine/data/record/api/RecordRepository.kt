package com.ignatlegostaev.inhabitroutine.data.record.api

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface RecordRepository {
    fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordModel>>
    fun readRecordsByTaskId(taskId: String): Flow<List<RecordModel>>
    suspend fun saveRecord(recordModel: RecordModel): ResultModel<Unit, Throwable>
    suspend fun deleteRecordByTaskIdAndDate(
        taskId: String,
        targetDate: LocalDate
    ): ResultModel<Unit, Throwable>
    suspend fun deleteRecordsByTaskIdAndPeriod(
        taskId: String,
        minDate: LocalDate,
        maxDate: LocalDate?
    ): ResultModel<Unit, Throwable>
    suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit, Throwable>
}