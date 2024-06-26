package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState

@Immutable
data class PickTaskTitleScreenState(
    val inputTitle: String,
    val canConfirm: Boolean
): ScreenState
