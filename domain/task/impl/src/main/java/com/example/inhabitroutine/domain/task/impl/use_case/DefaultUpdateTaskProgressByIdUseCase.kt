package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.core.util.todayDate
import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.model.task.content.TaskProgress
import com.example.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal class DefaultUpdateTaskProgressByIdUseCase(
    private val taskRepository: TaskRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : UpdateTaskProgressByIdUseCase {
    override suspend operator fun invoke(
        taskId: String,
        taskProgress: TaskProgress
    ): ResultModel<Unit, Throwable> = withContext(defaultDispatcher) {
        taskRepository.readTaskById(taskId)
            .filterIsInstance<TaskModel.Habit.HabitContinuous>()
            .firstOrNull()?.let { taskModel ->
                val newTaskModel: TaskModel = when (taskModel) {
                    is TaskModel.Habit.HabitContinuous.HabitNumber -> {
                        (taskProgress as? TaskProgress.Number)?.let { progress ->
                            taskModel.copy(
                                progress = progress,
                                versionStartDate = Clock.System.todayDate
                            )
                        } ?: return@withContext ResultModel.failure(IllegalArgumentException())
                    }

                    is TaskModel.Habit.HabitContinuous.HabitTime -> {
                        (taskProgress as? TaskProgress.Time)?.let { progress ->
                            taskModel.copy(
                                progress = progress,
                                versionStartDate = Clock.System.todayDate
                            )
                        } ?: return@withContext ResultModel.failure(IllegalArgumentException())
                    }
                }
                taskRepository.saveTask(newTaskModel)
            } ?: ResultModel.failure(NoSuchElementException())
    }
}