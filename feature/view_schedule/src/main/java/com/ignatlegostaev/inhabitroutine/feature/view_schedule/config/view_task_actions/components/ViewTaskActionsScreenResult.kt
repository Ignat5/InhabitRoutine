package com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.view_task_actions.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import kotlinx.datetime.LocalDate

sealed interface ViewTaskActionsScreenResult : ScreenResult {
    sealed interface OnActionClick : ViewTaskActionsScreenResult {
        val taskId: String
        val date: LocalDate

        data class EnterProgress(
            override val taskId: String,
            override val date: LocalDate
        ) : OnActionClick

        data class Done(
            override val taskId: String,
            override val date: LocalDate
        ) : OnActionClick

        data class Skip(
            override val taskId: String,
            override val date: LocalDate
        ) : OnActionClick

        data class Fail(
            override val taskId: String,
            override val date: LocalDate
        ) : OnActionClick

        data class ResetEntry(
            override val taskId: String,
            override val date: LocalDate
        ) : OnActionClick
    }

    data class OnEditClick(
        val taskId: String
    ) : ViewTaskActionsScreenResult

    data object Dismiss : ViewTaskActionsScreenResult
}