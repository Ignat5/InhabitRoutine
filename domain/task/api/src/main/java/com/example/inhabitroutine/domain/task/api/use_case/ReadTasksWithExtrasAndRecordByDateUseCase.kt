package com.example.inhabitroutine.domain.task.api.use_case

import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface ReadTasksWithExtrasAndRecordByDateUseCase {
    operator fun invoke(date: LocalDate): Flow<List<TaskWithExtrasAndRecordModel>>
}