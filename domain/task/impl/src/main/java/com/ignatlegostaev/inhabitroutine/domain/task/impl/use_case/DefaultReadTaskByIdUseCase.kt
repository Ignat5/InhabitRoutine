package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.data.task.api.TaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import kotlinx.coroutines.flow.Flow

internal class DefaultReadTaskByIdUseCase(
    private val taskRepository: TaskRepository
) : ReadTaskByIdUseCase {

    override fun invoke(taskId: String): Flow<TaskModel?> =
        taskRepository.readTaskById(taskId)

}