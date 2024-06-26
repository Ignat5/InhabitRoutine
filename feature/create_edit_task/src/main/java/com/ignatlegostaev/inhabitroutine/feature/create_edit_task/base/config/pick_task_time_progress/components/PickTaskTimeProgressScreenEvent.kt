package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType

sealed interface PickTaskTimeProgressScreenEvent : ScreenEvent {
    data class OnPickLimitType(
        val limitType: ProgressLimitType
    ) : PickTaskTimeProgressScreenEvent

    data class OnInputHoursUpdate(val hours: Int) : PickTaskTimeProgressScreenEvent
    data class OnInputMinutesUpdate(val minutes: Int) : PickTaskTimeProgressScreenEvent
    data object OnConfirmClick : PickTaskTimeProgressScreenEvent
    data object OnDismissRequest : PickTaskTimeProgressScreenEvent
}