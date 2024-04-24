package com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface ViewTaskActionsScreenResult : ScreenResult {
    val taskId: String

    data class OnTaskProgressClick(
        override val taskId: String
    ) : ViewTaskActionsScreenResult

    data class OnDoneClick(
        override val taskId: String
    ) : ViewTaskActionsScreenResult

    data class OnSkipClick(
        override val taskId: String
    ) : ViewTaskActionsScreenResult

    data class OnFailClick(
        override val taskId: String
    ) : ViewTaskActionsScreenResult

    data class OnResetEntryClick(
        override val taskId: String
    ) : ViewTaskActionsScreenResult


    data class OnEditTaskClick(
        override val taskId: String
    ) : ViewTaskActionsScreenResult

    data class Dismiss(
        override val taskId: String
    ) : ViewTaskActionsScreenResult
}