package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType

@Immutable
data class PickTaskNumberProgressScreenState(
    val inputLimitType: ProgressLimitType,
    val inputLimitNumber: String,
    val inputLimitUnit: String,
    val canConfirm: Boolean,
    val limitNumberInputValidator: (String) -> Boolean
) : ScreenState
