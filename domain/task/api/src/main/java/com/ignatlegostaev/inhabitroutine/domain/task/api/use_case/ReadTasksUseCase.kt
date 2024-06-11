package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.coroutines.flow.Flow

interface ReadTasksUseCase {
    operator fun invoke(excludeDrafts: Boolean = true): Flow<List<TaskModel.Task>>
}