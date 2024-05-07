package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.config.ConfirmLeavingScreenResult

sealed interface CreateTaskScreenEvent : ScreenEvent {
    data class Base(val baseEvent: BaseCreateEditTaskScreenEvent) : CreateTaskScreenEvent
    data object OnSaveClick : CreateTaskScreenEvent
    data object OnLeaveRequest : CreateTaskScreenEvent

    sealed interface ResultEvent : CreateTaskScreenEvent {
        val result: ScreenResult

        data class ConfirmLeaving(
            override val result: ConfirmLeavingScreenResult
        ) : ResultEvent
    }
}