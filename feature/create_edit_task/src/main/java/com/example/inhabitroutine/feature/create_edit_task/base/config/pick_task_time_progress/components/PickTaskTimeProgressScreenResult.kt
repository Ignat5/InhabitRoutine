package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.domain.model.task.content.TaskProgress

interface PickTaskTimeProgressScreenResult : ScreenResult {
    data class Confirm(
        val taskProgress: TaskProgress.Time
    ) : PickTaskTimeProgressScreenResult

    data object Dismiss : PickTaskTimeProgressScreenResult
}