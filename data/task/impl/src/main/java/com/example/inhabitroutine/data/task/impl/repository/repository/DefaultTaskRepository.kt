package com.example.inhabitroutine.data.task.impl.repository.repository

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.data.task.impl.repository.data_source.TaskDataSource
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskModel
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskDate
import kotlinx.coroutines.CoroutineDispatcher
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

    override suspend fun saveTask(
        taskModel: TaskModel,
        versionStartDate: LocalDate
    ): ResultModel<Unit, Throwable> =
        taskModel.toTaskDataModel(versionStartDate).let { taskDataModel ->
            taskDataSource.saveTask(taskDataModel)
        }

    override suspend fun updateTaskTitleById(
        taskId: String,
        title: String
    ): ResultModel<Unit, Throwable> = taskDataSource.updateTaskTitleById(
        taskId = taskId,
        title = title
    )

    override suspend fun updateTaskDateById(
        taskId: String,
        taskDate: TaskDate
    ): ResultModel<Unit, Throwable> {
        val startDate = when (taskDate) {
            is TaskDate.Day -> taskDate.date
            is TaskDate.Period -> taskDate.startDate
        }
        val endDate = when (taskDate) {
            is TaskDate.Day -> taskDate.date
            is TaskDate.Period -> taskDate.endDate
        }

        return taskDataSource.updateTaskStartEndDateById(
            taskId = taskId,
            startDate = startDate,
            endDate = endDate
        )
    }

    override suspend fun deleteTaskById(taskId: String): ResultModel<Unit, Throwable> =
        taskDataSource.deleteTaskById(taskId)
}