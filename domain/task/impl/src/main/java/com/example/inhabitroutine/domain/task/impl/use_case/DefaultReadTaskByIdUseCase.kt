package com.example.inhabitroutine.domain.task.impl.use_case

import com.example.inhabitroutine.data.task.api.TaskRepository
import com.example.inhabitroutine.domain.model.TaskModel
import com.example.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultReadTaskByIdUseCase(
    private val taskRepository: TaskRepository
): ReadTaskByIdUseCase {

    override fun invoke(taskId: String): Flow<TaskModel?> {
        return flow { emit(null) }
    }

}