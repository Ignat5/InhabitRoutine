package com.ignatlegostaev.inhabitroutine.data.record.impl.data_source

import com.ignatlegostaev.inhabitroutine.core.database.record.api.RecordDao
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.record.impl.model.RecordDataModel
import com.ignatlegostaev.inhabitroutine.data.record.impl.util.encodeToEpochDay
import com.ignatlegostaev.inhabitroutine.data.record.impl.util.toRecordDataModel
import com.ignatlegostaev.inhabitroutine.data.record.impl.util.toRecordEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
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

    override suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit, Throwable> =
        recordDao.deleteRecordsByTaskId(taskId)

}