package com.example.inhabitroutine.domain.task.api.use_case

import com.example.inhabitroutine.domain.model.task.TaskModel
import kotlinx.coroutines.flow.Flow

interface ReadHabitsUseCase {
    operator fun invoke(): Flow<List<TaskModel.Habit>>
}