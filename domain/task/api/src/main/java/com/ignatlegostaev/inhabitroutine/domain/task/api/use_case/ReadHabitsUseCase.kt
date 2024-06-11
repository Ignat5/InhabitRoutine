package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.coroutines.flow.Flow

interface ReadHabitsUseCase {
    operator fun invoke(excludeDrafts: Boolean = true): Flow<List<TaskModel.Habit>>
}