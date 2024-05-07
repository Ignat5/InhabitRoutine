package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.coroutines.flow.Flow

interface ReadTasksByQueryUseCase {
    operator fun invoke(query: String): Flow<List<TaskModel>>
}