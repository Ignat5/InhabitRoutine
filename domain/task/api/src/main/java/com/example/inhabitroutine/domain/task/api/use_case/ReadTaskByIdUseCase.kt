package com.example.inhabitroutine.domain.task.api.use_case

import com.example.inhabitroutine.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface ReadTaskByIdUseCase {
    operator fun invoke(taskId: String): Flow<TaskModel?>
}