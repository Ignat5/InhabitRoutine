package com.example.inhabitroutine.core.presentation.ui.dialog.archive_task

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenEvent
import com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ArchiveTaskStateHolder(
    private val taskId: String,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<ArchiveTaskScreenEvent, ArchiveTaskScreenState, ArchiveTaskScreenResult>() {

    override val uiScreenState: StateFlow<ArchiveTaskScreenState> =
        MutableStateFlow(ArchiveTaskScreenState)

    override fun onEvent(event: ArchiveTaskScreenEvent) {
        when (event) {
            is ArchiveTaskScreenEvent.OnConfirmClick -> onConfirmClick()
            is ArchiveTaskScreenEvent.OnDismissRequest -> onDismissRequest()
        }
    }

    private fun onConfirmClick() {
        setUpResult(ArchiveTaskScreenResult.Confirm(taskId))
    }

    private fun onDismissRequest() {
        setUpResult(ArchiveTaskScreenResult.Dismiss)
    }

}