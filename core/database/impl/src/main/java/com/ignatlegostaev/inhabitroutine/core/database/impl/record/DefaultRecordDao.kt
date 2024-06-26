package com.ignatlegostaev.inhabitroutine.core.database.impl.record

import com.ignatlegostaev.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.ignatlegostaev.inhabitroutine.core.database.impl.util.readQueryList
import com.ignatlegostaev.inhabitroutine.core.database.impl.util.runQuery
import com.ignatlegostaev.inhabitroutine.core.database.impl.util.toRecordEntity
import com.ignatlegostaev.inhabitroutine.core.database.impl.util.toRecordTable
import com.ignatlegostaev.inhabitroutine.core.database.record.api.RecordDao
import com.ignatlegostaev.inhabitroutine.core.database.record.api.RecordEntity
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
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

    override fun readRecordsByTaskId(taskId: String): Flow<List<RecordEntity>> =
        recordDao.selectRecordsByTaskId(taskId).readQueryList(ioDispatcher)
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

    override suspend fun deleteRecordByTaskIdAndDate(
        taskId: String,
        targetEpochDay: Long
    ): ResultModel<Unit, Throwable> =
        runQuery(ioDispatcher) {
            recordDao.deleteRecordByTaskIdAndDate(
                taskId = taskId,
                targetEpochDay = targetEpochDay
            )
        }

    override suspend fun deleteRecordsByTaskIdAndPeriod(
        taskId: String,
        minEpochDay: Long,
        maxEpochDay: Long
    ): ResultModel<Unit, Throwable> = runQuery(ioDispatcher) {
        recordDao.deleteRecordsByTaskIdAndPeriod(
            taskId = taskId,
            minEpochDay = minEpochDay,
            maxEpochDay = maxEpochDay
        )
    }

    override suspend fun deleteRecordsByTaskId(taskId: String): ResultModel<Unit, Throwable> =
        runQuery(ioDispatcher) {
            recordDao.deleteRecordsByTaskId(taskId)
        }

}