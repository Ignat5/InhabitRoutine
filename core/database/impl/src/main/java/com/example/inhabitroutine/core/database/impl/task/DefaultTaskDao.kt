package com.example.inhabitroutine.core.database.impl.task

import com.example.inhabitroutine.core.database.impl.InhabitRoutineDatabase
import com.example.inhabitroutine.core.database.impl.util.readOneOrNull
import com.example.inhabitroutine.core.database.impl.util.readQueryList
import com.example.inhabitroutine.core.database.impl.util.runQuery
import com.example.inhabitroutine.core.database.impl.util.runTransaction
import com.example.inhabitroutine.core.database.impl.util.toTaskContentTable
import com.example.inhabitroutine.core.database.impl.util.toTaskEntity
import com.example.inhabitroutine.core.database.impl.util.toTaskTable
import com.example.inhabitroutine.core.database.task.api.TaskDao
import com.example.inhabitroutine.core.database.task.api.TaskEntity
import com.example.inhabitroutine.core.util.ResultModel
import database.TaskView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class DefaultTaskDao(
    private val db: InhabitRoutineDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : TaskDao {

    private val taskDao = db.taskDaoQueries

    override fun readTaskById(taskId: String): Flow<TaskEntity?> =
        taskDao.selectTaskById(taskId).readOneOrNull(ioDispatcher).map { taskTable ->
            taskTable?.toTaskEntity()
        }

    override fun readTasksByQuery(query: String): Flow<List<TaskEntity>> =
        taskDao.selectTasksByQuery("%$query%").readQueryList(ioDispatcher)
            .map { allTasks ->
                if (allTasks.isNotEmpty()) {
                    withContext(ioDispatcher) {
                        allTasks.map { it.toTaskEntity() }
                    }
                } else emptyList()
            }

    override fun readTasksByDate(targetEpochDay: Long): Flow<List<TaskEntity>> =
        taskDao.selectTasksByDate(targetEpochDay = targetEpochDay).readQueryList(ioDispatcher)
            .map { allTasks ->
                if (allTasks.isNotEmpty()) {
                    withContext(ioDispatcher) {
                        allTasks.map { it.toTaskEntity() }
                    }
                } else emptyList()
            }

    override fun readTasksById(taskId: String): Flow<List<TaskEntity>> =
        taskDao.selectTasksById(taskId).readQueryList(ioDispatcher)
            .map { allTasks ->
                if (allTasks.isNotEmpty()) {
                    withContext(ioDispatcher) {
                        allTasks.map { it.toTaskEntity() }
                    }
                } else emptyList()
            }

    override suspend fun saveTask(taskEntity: TaskEntity): ResultModel<Unit, Throwable> =
        db.runTransaction(ioDispatcher) {
            taskDao.apply {
                insertTask(taskEntity.toTaskTable())
                insertTaskContent(taskEntity.toTaskContentTable())
            }
        }

    override suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable> = runQuery(ioDispatcher) {
        taskDao.updateTaskTitleById(
            taskId = taskId,
            title = title
        )
    }

    override suspend fun updateTaskStartEndDateById(
        taskId: String,
        startEpochDay: Long,
        endEpochDay: Long
    ): ResultModel<Unit, Throwable> = db.runTransaction(ioDispatcher) {
        taskDao.updateTaskStartEndDateById(
            taskId = taskId,
            startEpochDay = startEpochDay,
            endEpochDay = endEpochDay,
        )
    }

    override suspend fun updateTaskDescriptionById(
        taskId: String,
        description: String
    ): ResultModel<Unit, Throwable> = runQuery(ioDispatcher) {
        taskDao.updateTaskDescriptionById(
            taskId = taskId,
            description = description
        )
    }

    override suspend fun updateTaskIsDraftById(
        taskId: String,
        isDraft: String
    ): ResultModel<Unit, Throwable> = runQuery(ioDispatcher) {
        taskDao.updateTaskIsDraftById(
            taskId = taskId,
            isDraft = isDraft
        )
    }

    override suspend fun deleteTaskById(
        taskId: String
    ): ResultModel<Unit, Throwable> = runQuery(ioDispatcher) {
        taskDao.deleteTaskById(taskId)
    }
}