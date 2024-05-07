package com.ignatlegostaev.inhabitroutine.data.task.impl.repository.repository

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.util.toTaskDataModel
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.util.toTaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

internal class DefaultTaskRepository(
    private val taskDataSource: TaskDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) : TaskRepository {

    override fun readTaskById(taskId: String): Flow<TaskModel?> =
        taskDataSource.readTaskById(taskId).map { taskEntity ->
            if (taskEntity != null) {
                withContext(defaultDispatcher) {
                    taskEntity.toTaskModel()
                }
            } else null
        }

    override fun readTasksByQuery(query: String): Flow<List<TaskModel>> =
        taskDataSource.readTasksByQuery(query).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allTasks.map {
                        async { it.toTaskModel() }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readTasksByDate(targetDate: LocalDate): Flow<List<TaskModel>> =
        taskDataSource.readTasksByDate(targetDate).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allTasks.map {
                        async { it.toTaskModel() }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readTasksById(taskId: String): Flow<List<TaskModel>> =
        taskDataSource.readTasksById(taskId).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allTasks.map {
                        async { it.toTaskModel() }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override fun readTasksByTaskType(targetTaskTypes: Set<TaskType>): Flow<List<TaskModel>> =
        taskDataSource.readTasksByTaskType(targetTaskTypes).map { allTasks ->
            if (allTasks.isNotEmpty()) {
                withContext(defaultDispatcher) {
                    allTasks.map {
                        async { it.toTaskModel() }
                    }.awaitAll().filterNotNull()
                }
            } else emptyList()
        }

    override suspend fun saveTask(
        taskModel: TaskModel
    ): ResultModel<Unit, Throwable> =
        taskModel.toTaskDataModel().let { taskDataModel ->
            taskDataSource.saveTask(taskDataModel)
        }

    override suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable> =
        taskDataSource.deleteTaskById(taskId)

}