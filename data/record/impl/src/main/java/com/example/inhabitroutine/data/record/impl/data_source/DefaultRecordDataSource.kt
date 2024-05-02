package com.example.inhabitroutine.data.record.impl.data_source

import com.example.inhabitroutine.core.database.record.api.RecordDao
import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.record.impl.model.RecordDataModel
import com.example.inhabitroutine.data.record.impl.util.distantFutureDate
import com.example.inhabitroutine.data.record.impl.util.encodeToEpochDay
import com.example.inhabitroutine.data.record.impl.util.toRecordDataModel
import com.example.inhabitroutine.data.record.impl.util.toRecordEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json

internal class DefaultRecordDataSource(
    private val recordDao: RecordDao,
    private val json: Json,
    private val ioDispatcher: CoroutineDispatcher
) : RecordDataSource {

    override fun readRecordsByDate(targetDate: LocalDate): Flow<List<RecordDataModel>> =
        recordDao.readRecordsByDate(targetDate.encodeToEpochDay()).map { allRecords ->
            if (allRecords.isNotEmpty()) {
                withContext(ioDispatcher) {
                    allRecords.map {
                        async { it.toRecordDataModel(json) }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readRecordsByTaskId(taskId: String): Flow<List<RecordDataModel>> =
        recordDao.readRecordsByTaskId(taskId).map { allRecords ->
            if (allRecords.isNotEmpty()) {
                withContext(ioDispatcher) {
                    allRecords.map {
                        async { it.toRecordDataModel(json) }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override suspend fun saveRecord(recordDataModel: RecordDataModel): ResultModel<Unit, Throwable> =
        recordDataModel.toRecordEntity(json)?.let { recordEntity ->
            recordDao.saveRecord(recordEntity)
        } ?: ResultModel.failure(IllegalStateException())

    override suspend fun deleteRecordByTaskIdAndDate(
        taskId: String,
        targetDate: LocalDate
    ): ResultModel<Unit, Throwable> = recordDao.deleteRecordByTaskIdAndDate(
        taskId = taskId,
        targetEpochDay = targetDate.encodeToEpochDay()
    )

    override suspend fun deleteRecordsByTaskIdAndPeriod(
        taskId: String,
        minDate: LocalDate,
        maxDate: LocalDate?
    ): ResultModel<Unit, Throwable> = recordDao.deleteRecordsByTaskIdAndPeriod(
        taskId = taskId,
        minEpochDay = minDate.encodeToEpochDay(),
        maxEpochDay = (maxDate ?: distantFutureDate).encodeToEpochDay()
    )

    override suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit, Throwable> =
        recordDao.deleteRecordsByTaskId(taskId)

}