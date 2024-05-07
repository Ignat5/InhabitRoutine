package com.ignatlegostaev.inhabitroutine.data.task.impl.repository.data_source

import com.ignatlegostaev.inhabitroutine.core.database.task.api.TaskDao
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.util.encodeToEpochDay
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.util.encodeToString
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.util.toTaskDataModel
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.util.toTaskEntity
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

internal class DefaultTaskDataSource(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val json: Json
) : TaskDataSource {

    override fun readTaskById(taskId: String): Flow<TaskDataModel?> =
        taskDao.readTaskById(taskId).map { taskEntity ->
            withContext(ioDispatcher) {
                taskEntity?.toTaskDataModel(json)
            }
        }

    override fun readTasksByQuery(query: String): Flow<List<TaskDataModel>> =
        taskDao.readTasksByQuery(query).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(ioDispatcher) {
                    allTasks.map {
                        async { it.toTaskDataModel(json) }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readTasksByDate(targetDate: LocalDate): Flow<List<TaskDataModel>> =
        taskDao.readTasksByDate(targetEpochDay = targetDate.encodeToEpochDay()).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(ioDispatcher) {
                    allTasks.map {
                        async { it.toTaskDataModel(json) }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readTasksById(taskId: String): Flow<List<TaskDataModel>> =
        taskDao.readTasksById(taskId).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(ioDispatcher) {
                    allTasks.map {
                        async { it.toTaskDataModel(json) }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readTasksByTaskType(targetTaskTypes: Set<TaskType>): Flow<List<TaskDataModel>> =
        taskDao.readTasksByTaskType(targetTaskTypes.encodeToString(json).toSet()).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(ioDispatcher) {
                    allTasks.map {
                        async { it.toTaskDataModel(json) }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override suspend fun saveTask(taskDataModel: TaskDataModel): ResultModel<Unit, Throwable> =
        withContext(ioDispatcher) {
            taskDataModel.toTaskEntity(json)?.let { taskEntity ->
                taskDao.saveTask(taskEntity)
            } ?: ResultModel.failure(IllegalStateException())
        }

    override suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable> =
        taskDao.deleteTaskById(taskId)
}