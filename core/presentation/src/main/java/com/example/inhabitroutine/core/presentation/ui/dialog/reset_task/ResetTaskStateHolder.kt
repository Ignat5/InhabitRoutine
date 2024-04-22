package com.example.inhabitroutine.core.presentation.ui.dialog.reset_task

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenEvent
import com.example.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.reset_task.components.ResetTaskScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResetTaskStateHolder(
    private val taskId: String,
    override val holderScope: CoroutineScope
): BaseResultStateHolder<ResetTaskScreenEvent, ResetTaskScreenState, ResetTaskScreenResult>() {

    override val uiScreenState: StateFlow<ResetTaskScreenState> =
        MutableStateFlow(ResetTaskScreenState)

    override fun onEvent(event: ResetTaskScreenEvent) {
        when (event) {
            is ResetTaskScreenEvent.OnConfirmClick -> onConfirmClick()
            is ResetTaskScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onConfirmClick() {
        setUpResult(ResetTaskScreenResult.Confirm(taskId))
    }

    private fun onDismissRequest() {
        setUpResult(ResetTaskScreenResult.Dismiss)
    }

}