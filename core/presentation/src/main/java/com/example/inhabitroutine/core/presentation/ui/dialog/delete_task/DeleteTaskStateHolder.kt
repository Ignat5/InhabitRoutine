package com.example.inhabitroutine.core.presentation.ui.dialog.delete_task

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenEvent
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeleteTaskStateHolder(
    private val taskId: String,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<DeleteTaskScreenEvent, DeleteTaskScreenState, DeleteTaskScreenResult>() {

    override val uiScreenState: StateFlow<DeleteTaskScreenState> =
        MutableStateFlow(DeleteTaskScreenState)

    override fun onEvent(event: DeleteTaskScreenEvent) {
        when (event) {
            is DeleteTaskScreenEvent.OnConfirmClick -> onConfirmClick()
            is DeleteTaskScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onConfirmClick() {
        setUpResult(DeleteTaskScreenResult.Confirm(taskId))
    }

    private fun onDismissRequest() {
        setUpResult(DeleteTaskScreenResult.Dismiss)
    }

}