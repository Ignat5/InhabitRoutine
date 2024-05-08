package com.ignatlegostaev.inhabitroutine.data.record.impl.data_source

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.record.impl.model.RecordDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface RecordDataSource {
    fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordDataModel>>
    fun readRecordsByTaskId(taskId: String): Flow<List<RecordDataModel>>
    suspend fun saveRecord(recordDataModel: RecordDataModel): ResultModel<Unit, Throwable>
    suspend fun deleteRecordByTaskIdAndDate(
        taskId: String,
        targetDate: LocalDate
    ): ResultModel<Unit, Throwable>
    suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit, Throwable>
}