package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency

@Immutable
data class PickTaskFrequencyScreenState(
    val inputTaskFrequency: TaskFrequency,
    val canConfirm: Boolean
) : ScreenState
