package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency

sealed interface PickTaskFrequencyScreenResult : ScreenResult {
    data class Confirm(
        val taskFrequency: TaskFrequency
    ) : PickTaskFrequencyScreenResult

    data object Dismiss : PickTaskFrequencyScreenResult
}