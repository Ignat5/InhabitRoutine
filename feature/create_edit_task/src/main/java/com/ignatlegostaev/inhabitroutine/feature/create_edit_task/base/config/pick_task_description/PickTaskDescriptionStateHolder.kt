package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskDescriptionStateHolder(
    initDescription: String,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskDescriptionScreenEvent, PickTaskDescriptionScreenState, PickTaskDescriptionScreenResult>() {

    private val inputDescriptionState = MutableStateFlow(initDescription)

    override val uiScreenState: StateFlow<PickTaskDescriptionScreenState> =
        inputDescriptionState.map { inputDescription ->
            PickTaskDescriptionScreenState(
                inputDescription = inputDescription
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            PickTaskDescriptionScreenState(
                inputDescription = inputDescriptionState.value
            )
        )

    override fun onEvent(event: PickTaskDescriptionScreenEvent) {
        when (event) {
            is PickTaskDescriptionScreenEvent.OnInputDescriptionUpdate ->
                onInputDescriptionUpdate(event)

            is PickTaskDescriptionScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is PickTaskDescriptionScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onInputDescriptionUpdate(event: PickTaskDescriptionScreenEvent.OnInputDescriptionUpdate) {
        inputDescriptionState.update { event.value }
    }

    private fun onConfirmClick() {
        setUpResult(PickTaskDescriptionScreenResult.Confirm(inputDescriptionState.value))
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskDescriptionScreenResult.Dismiss)
    }

}