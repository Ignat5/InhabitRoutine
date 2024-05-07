package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.util.getTaskVersionStartDate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

internal class DefaultUpdateTaskProgressByIdUseCase(
    private val taskRepository: TaskRepository, private val defaultDispatcher: CoroutineDispatcher
) : UpdateTaskProgressByIdUseCase {
    override suspend operator fun invoke(
        taskId: String, taskProgress: TaskProgress
    ): ResultModel<Unit, Throwable> = withContext(defaultDispatcher) {
        taskRepository.readTaskById(taskId).filterIsInstance<TaskModel.Habit.HabitContinuous>()
            .firstOrNull()?.let { taskModel ->
                taskModel.copy(
                    progress = taskProgress,
                    versionStartDate = taskModel.getTaskVersionStartDate()
                ).let { newTaskModel ->
                    taskRepository.saveTask(newTaskModel)
                }
            } ?: ResultModel.failure(NoSuchElementException())
    }
}