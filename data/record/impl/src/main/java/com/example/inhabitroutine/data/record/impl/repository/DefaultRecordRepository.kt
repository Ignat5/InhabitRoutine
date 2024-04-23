package com.example.inhabitroutine.data.record.impl.repository

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.record.api.RecordRepository
import com.example.inhabitroutine.data.record.impl.data_source.RecordDataSource
import com.example.inhabitroutine.data.record.impl.util.toRecordDataModel
import com.example.inhabitroutine.data.record.impl.util.toRecordModel
import com.example.inhabitroutine.domain.model.record.RecordModel
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

    override suspend fun saveRecord(recordModel: RecordModel): ResultModel<Unit, Throwable> =
        recordDataSource.saveRecord(recordModel.toRecordDataModel())

}