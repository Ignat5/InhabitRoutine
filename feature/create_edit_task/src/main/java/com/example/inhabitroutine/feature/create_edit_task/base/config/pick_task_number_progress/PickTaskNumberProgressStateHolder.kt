package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.core.presentation.ui.util.limitNumberToDisplay
import com.example.inhabitroutine.domain.model.task.content.TaskProgress
import com.example.inhabitroutine.domain.task.api.use_case.ValidateProgressLimitNumberUseCase
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskNumberProgressStateHolder(
    initTaskProgress: TaskProgress.Number,
    private val validateProgressLimitNumberUseCase: ValidateProgressLimitNumberUseCase,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskNumberProgressScreenEvent, PickTaskNumberProgressScreenState, PickTaskNumberProgressScreenResult>() {

    private val inputLimitTypeState = MutableStateFlow(initTaskProgress.limitType)
    private val inputLimitNumberState = MutableStateFlow(initTaskProgress.limitNumber.limitNumberToDisplay())
    private val inputLimitUnitState = MutableStateFlow(initTaskProgress.limitUnit)
    private val limitNumberInputValidator: (String) -> Boolean = { input ->
        input.isEmpty() || input.isValid()
    }
    private val canConfirmFlow = inputLimitNumberState.map { input ->
        input.isValid()
    }

    override val uiScreenState: StateFlow<PickTaskNumberProgressScreenState> =
        combine(
            inputLimitTypeState,
            inputLimitNumberState,
            inputLimitUnitState,
            canConfirmFlow
        ) { inputLimitType, inputLimitNumber, inputLimitUnit, canConfirm ->
            PickTaskNumberProgressScreenState(
                inputLimitType = inputLimitType,
                inputLimitNumber = inputLimitNumber,
                inputLimitUnit = inputLimitUnit,
                canConfirm = canConfirm,
                limitNumberInputValidator = limitNumberInputValidator
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            PickTaskNumberProgressScreenState(
                inputLimitType = inputLimitTypeState.value,
                inputLimitNumber = inputLimitNumberState.value,
                inputLimitUnit = inputLimitUnitState.value,
                canConfirm = inputLimitNumberState.value.isValid(),
                limitNumberInputValidator = limitNumberInputValidator
            )
        )

    override fun onEvent(event: PickTaskNumberProgressScreenEvent) {
        when (event) {
            is PickTaskNumberProgressScreenEvent.OnInputLimitNumberUpdate ->
                onInputLimitNumberUpdate(event)

            is PickTaskNumberProgressScreenEvent.OnInputLimitUnitUpdate ->
                onInputLimitUnitUpdate(event)

            is PickTaskNumberProgressScreenEvent.OnPickProgressLimitType ->
                onPickProgressLimitType(event)

            is PickTaskNumberProgressScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is PickTaskNumberProgressScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onInputLimitNumberUpdate(event: PickTaskNumberProgressScreenEvent.OnInputLimitNumberUpdate) {
        event.value.let { input ->
            inputLimitNumberState.update { input }
        }
    }

    private fun onInputLimitUnitUpdate(event: PickTaskNumberProgressScreenEvent.OnInputLimitUnitUpdate) {
        inputLimitUnitState.update { event.value }
    }

    private fun onPickProgressLimitType(event: PickTaskNumberProgressScreenEvent.OnPickProgressLimitType) {
        inputLimitTypeState.update { event.progressLimitType }
    }

    private fun onConfirmClick() {
        inputLimitNumberState.value.let { inputLimitNumber ->
            if (inputLimitNumber.isValid()) {
                inputLimitNumber.toDoubleOrNull()?.let { limitNumber ->
                    setUpResult(
                        PickTaskNumberProgressScreenResult.Confirm(
                            TaskProgress.Number(
                                limitType = inputLimitTypeState.value,
                                limitNumber = limitNumber,
                                limitUnit = inputLimitUnitState.value
                            )
                        )
                    )
                }
            }
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskNumberProgressScreenResult.Dismiss)
    }

    private fun String.isValid(): Boolean = this.let { input ->
        input.toDoubleOrNull()?.let { validateProgressLimitNumberUseCase(it) } ?: false
    }

}