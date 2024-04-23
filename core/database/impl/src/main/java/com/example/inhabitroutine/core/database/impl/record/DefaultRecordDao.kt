package com.example.inhabitroutine.core.database.impl.record

import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.util.readQueryList
import com.example.inhabitroutine.core.database.impl.util.runQuery
import com.example.inhabitroutine.core.database.impl.util.toRecordEntity
import com.example.inhabitroutine.core.database.impl.util.toRecordTable
import com.example.inhabitroutine.core.database.record.api.RecordDao
import com.example.inhabitroutine.core.database.record.api.RecordEntity
import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultRecordDao(
    private val db: InhabitRoutineDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : RecordDao {

    private val recordDao = db.recordDaoQueries

    override fun readRecordsByDate(targetEpochDay: Long): Flow<List<RecordEntity>> =
        recordDao.selectRecordsByDate(targetEpochDay).readQueryList(ioDispatcher)
            .map { allReminders ->
                if (allReminders.isNotEmpty()) {
                    withContext(ioDispatcher) {
                        allReminders.map { it.toRecordEntity() }
                    }
                } else emptyList()
            }

    override suspend fun saveRecord(recordEntity: RecordEntity): ResultModel<Unit, Throwable> =
        runQuery(ioDispatcher) {
            recordDao.insertRecord(recordEntity.toRecordTable())
        }

}