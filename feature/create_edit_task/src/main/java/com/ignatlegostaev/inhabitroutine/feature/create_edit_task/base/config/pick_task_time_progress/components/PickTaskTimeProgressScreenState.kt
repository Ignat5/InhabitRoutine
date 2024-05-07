package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType

@Immutable
data class PickTaskTimeProgressScreenState(
    val inputLimitType: ProgressLimitType,
    val inputHours: Int,
    val inputMinutes: Int
) : ScreenState
