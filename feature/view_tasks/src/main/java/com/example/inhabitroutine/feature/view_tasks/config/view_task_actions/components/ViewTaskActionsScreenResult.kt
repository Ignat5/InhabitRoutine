package com.example.inhabitroutine.feature.view_tasks.config.view_task_actions.components

import com.example.inhabitroutine.core.presentation.components.result.ScreenResult

sealed interface ViewTaskActionsScreenResult : ScreenResult {
    sealed interface Action : ViewTaskActionsScreenResult {
        val taskId: String

        data class Edit(override val taskId: String) : Action
        data class Archive(override val taskId: String) : Action
        data class Unarchive(override val taskId: String) : Action
        data class Delete(override val taskId: String) : Action
    }

    data object Dismiss : ViewTaskActionsScreenResult
}