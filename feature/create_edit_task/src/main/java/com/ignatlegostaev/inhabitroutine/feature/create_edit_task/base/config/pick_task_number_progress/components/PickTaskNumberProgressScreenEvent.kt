package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType

sealed interface PickTaskNumberProgressScreenEvent : ScreenEvent {
    data class OnPickProgressLimitType(
        val progressLimitType: ProgressLimitType
    ) : PickTaskNumberProgressScreenEvent

    data class OnInputLimitNumberUpdate(
        val value: String
    ) : PickTaskNumberProgressScreenEvent

    data class OnInputLimitUnitUpdate(
        val value: String
    ) : PickTaskNumberProgressScreenEvent

    data object OnConfirmClick : PickTaskNumberProgressScreenEvent
    data object OnDismissRequest : PickTaskNumberProgressScreenEvent
}