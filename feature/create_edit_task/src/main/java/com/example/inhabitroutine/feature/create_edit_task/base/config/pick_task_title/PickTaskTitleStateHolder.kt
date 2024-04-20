package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskTitleStateHolder(
    initTitle: String,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskTitleScreenEvent, PickTaskTitleScreenState, PickTaskTitleScreenResult>() {

    private val inputTitleState = MutableStateFlow(initTitle)

    override val uiScreenState: StateFlow<PickTaskTitleScreenState> =
        inputTitleState.map { inputTitle ->
            PickTaskTitleScreenState(
                inputTitle = inputTitle,
                canConfirm = inputTitle.checkIfCanConfirm()
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            PickTaskTitleScreenState(
                inputTitle = inputTitleState.value,
                canConfirm = inputTitleState.value.checkIfCanConfirm()
            )
        )

    override fun onEvent(event: PickTaskTitleScreenEvent) {
        when (event) {
            is PickTaskTitleScreenEvent.OnInputValueUpdate ->
                onInputValueUpdate(event)

            is PickTaskTitleScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is PickTaskTitleScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onInputValueUpdate(event: PickTaskTitleScreenEvent.OnInputValueUpdate) {
        inputTitleState.update { event.value }
    }

    private fun onConfirmClick() {
        inputTitleState.value.let { inputTitle ->
            if (inputTitle.checkIfCanConfirm()) {
                setUpResult(PickTaskTitleScreenResult.Confirm(inputTitle))
            }
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskTitleScreenResult.Dismiss)
    }

    private fun String.checkIfCanConfirm() = this.isNotBlank()

}