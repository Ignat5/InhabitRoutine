package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress

interface PickTaskTimeProgressScreenResult : ScreenResult {
    data class Confirm(
        val taskProgress: TaskProgress.Time
    ) : PickTaskTimeProgressScreenResult

    data object Dismiss : PickTaskTimeProgressScreenResult
}