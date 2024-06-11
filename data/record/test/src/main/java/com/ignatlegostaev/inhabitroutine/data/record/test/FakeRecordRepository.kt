package com.ignatlegostaev.inhabitroutine.data.record.test

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.max

class FakeRecordRepository : RecordRepository {

    private val allRecordsState = MutableStateFlow<List<RecordModel>>(emptyList())

    fun setRecords(allRecords: List<RecordModel>) {
        allRecordsState.update { allRecords }
    }

    fun getRecords(): List<RecordModel> = allRecordsState.value

    override fun readRecordsByTaskId(taskId: String): Flow<List<RecordModel>> {
        return allRecordsState.map { allRecords ->
            allRecords.filter { it.taskId == taskId }
        }
    }

    override fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordModel>> {
        return allRecordsState
    }

    override suspend fun saveRecord(recordModel: RecordModel): ResultModel<Unit, Throwable> {
        allRecordsState.update { oldRecords ->
            val newRecords = mutableListOf<RecordModel>()
            newRecords.addAll(oldRecords)
            val index = newRecords.indexOfFirst { it.id == recordModel.id }
            if (index != -1) {
                newRecords[index] = recordModel
            } else {
                newRecords.add(recordModel)
            }
            newRecords
        }
        return ResultModel.success(Unit)
    }

    override suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit, Throwable> {
        deleteRecordsByPredicate { recordModel ->
            recordModel.taskId == taskId
        }
        return ResultModel.success(Unit)
    }

    override suspend fun deleteRecordsByTaskIdAndPeriod(
        taskId: String,
        minDate: LocalDate,
        maxDate: LocalDate?
    ): ResultModel<Unit, Throwable> {
        deleteRecordsByPredicate { recordModel ->
            if (maxDate != null) {
                recordModel.date !in minDate..maxDate
            } else {
                recordModel.date < minDate
            }
        }
        return ResultModel.success(Unit)
    }

    override suspend fun deleteRecordByTaskIdAndDate(
        taskId: String,
        targetDate: LocalDate
    ): ResultModel<Unit, Throwable> {
        deleteRecordsByPredicate { recordModel ->
            recordModel.date == targetDate
        }
        return ResultModel.success(Unit)
    }

    private fun deleteRecordsByPredicate(predicate: (RecordModel) -> Boolean) {
        allRecordsState.update { oldRecords ->
            val newRecords = mutableListOf<RecordModel>()
            newRecords.addAll(oldRecords)
            newRecords.removeIf(predicate)
            newRecords
        }
    }

}