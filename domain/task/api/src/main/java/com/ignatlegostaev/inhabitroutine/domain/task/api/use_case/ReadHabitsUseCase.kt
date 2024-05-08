package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.coroutines.flow.Flow

interface ReadHabitsUseCase {
    operator fun invoke(): Flow<List<TaskModel.Habit>>
}