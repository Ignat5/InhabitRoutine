package com.ignatlegostaev.inhabitroutine.data.record.impl.repository

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.data.record.impl.data_source.RecordDataSource
import com.ignatlegostaev.inhabitroutine.data.record.impl.util.toRecordDataModel
import com.ignatlegostaev.inhabitroutine.data.record.impl.util.toRecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

internal class DefaultRecordRepository(
    private val recordDataSource: RecordDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : RecordRepository {

    override fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordModel>> =
        recordDataSource.readRecordsByDate(targetDate).map { allRecords ->
            if (allRecords.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allRecords.map {
                        async { it.toRecordModel() }
                    }.awaitAll()
                }
            } else emptyList()
        }

    override fun readRecordsByTaskId(taskId: String): Flow<List<RecordModel>> =
        recordDataSource.readRecordsByTaskId(taskId).map { allRecords ->
            if (allRecords.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allRecords.map {
                        async { it.toRecordModel() }
                    }.awaitAll()
                }
            } else emptyList()
        }

    override suspend fun saveRecord(recordModel: RecordModel): ResultModel<Unit, Throwable> =
        recordDataSource.saveRecord(recordModel.toRecordDataModel())

    override suspend fun deleteRecordByTaskIdAndDate(
        taskId: String,
        targetDate: LocalDate
    ): ResultModel<Unit, Throwable> = recordDataSource.deleteRecordByTaskIdAndDate(
        taskId = taskId,
        targetDate = targetDate
    )

    override suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit, Throwable> =
        recordDataSource.deleteRecordsByTaskId(taskId)

}