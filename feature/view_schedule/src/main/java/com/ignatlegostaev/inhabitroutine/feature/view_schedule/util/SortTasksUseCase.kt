package com.ignatlegostaev.inhabitroutine.feature.view_schedule.util

import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel

interface SortTasksUseCase {
    operator fun invoke(allTasks: List<TaskWithExtrasAndRecordModel>): List<TaskWithExtrasAndRecordModel>
}