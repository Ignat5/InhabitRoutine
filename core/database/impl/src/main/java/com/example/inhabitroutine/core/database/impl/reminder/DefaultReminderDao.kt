package com.example.inhabitroutine.core.database.impl.reminder

import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.util.readOne
import com.example.inhabitroutine.core.database.impl.util.readOneOrNull
import com.example.inhabitroutine.core.database.impl.util.readQueryList
import com.example.inhabitroutine.core.database.impl.util.runQuery
import com.example.inhabitroutine.core.database.impl.util.toReminderEntity
import com.example.inhabitroutine.core.database.impl.util.toReminderTable
import com.example.inhabitroutine.core.database.reminder.api.ReminderDao
import com.example.inhabitroutine.core.database.reminder.api.ReminderEntity
import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultReminderDao(
    private val db: InhabitRoutineDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : ReminderDao {

    private val reminderDao = db.reminderDaoQueries

    override fun readRemindersByTaskId(taskId: String): Flow<List<ReminderEntity>> =
        reminderDao.selectRemindersByTaskId(taskId).readQueryList(ioDispatcher)
            .map { allReminders ->
                if (allReminders.isNotEmpty()) {
                    withContext(ioDispatcher) {
                        allReminders.map { reminderTable ->
                            reminderTable.toReminderEntity()
                        }
                    }
                } else emptyList()
            }

    override fun readReminderById(reminderId: String): Flow<ReminderEntity?> =
        reminderDao.selectReminderById(reminderId).readOneOrNull(ioDispatcher)
            .map { it?.toReminderEntity() }

    override fun readReminderCountByTaskId(taskId: String): Flow<Int> =
        reminderDao.selectReminderCountByTaskId(taskId)
            .readOne(ioDispatcher)
            .map { it.toInt() }

    override fun readRemindersByDate(targetEpochDay: Long): Flow<List<ReminderEntity>> =
        reminderDao.selectRemindersByDate(targetEpochDay = targetEpochDay)
            .readQueryList(ioDispatcher).map { allReminders ->
                if (allReminders.isNotEmpty()) {
                    allReminders.map { it.toReminderEntity() }
                } else emptyList()
            }

    override fun readReminders(): Flow<List<ReminderEntity>> =
        reminderDao.selectReminders().readQueryList(ioDispatcher).map { allReminders ->
            if (allReminders.isNotEmpty()) {
                withContext(ioDispatcher) {
                    allReminders.map { it.toReminderEntity() }
                }
            } else emptyList()
        }

    override fun readReminderIdsByTaskId(taskId: String): Flow<List<String>> =
        reminderDao.selectReminderIdsByTaskId(taskId).readQueryList(ioDispatcher)

    override suspend fun saveReminder(reminderEntity: ReminderEntity): ResultModel<Unit, Throwable> =
        runQuery(ioDispatcher) {
            reminderDao.insertReminder(reminderEntity.toReminderTable())
        }

    override suspend fun updateReminder(reminderEntity: ReminderEntity): ResultModel<Unit, Throwable> =
        saveReminder(reminderEntity)

    override suspend fun deleteReminderById(reminderId: String): ResultModel<Unit, Throwable> =
        runQuery(ioDispatcher) {
            reminderDao.deleteReminderById(reminderId)
        }

}