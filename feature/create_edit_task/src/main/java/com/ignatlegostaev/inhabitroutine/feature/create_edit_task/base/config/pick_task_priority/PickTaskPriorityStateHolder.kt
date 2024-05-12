package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.ignatlegostaev.inhabitroutine.domain.model.util.DomainConst
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PickTaskPriorityStateHolder(
    initPriority: Long,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskPriorityScreenEvent, PickTaskPriorityScreenState, PickTaskPriorityScreenResult>() {
    private val inputPriorityState = MutableStateFlow(initPriority.toString())
    private val availablePriorityRange by lazy {
        DomainConst.MIN_PRIORITY..DomainConst.MAX_PRIORITY
    }
    private val maxPriorityLength by lazy {
        DomainConst.MAX_PRIORITY.toString().length
    }
    private val canConfirmFlow = inputPriorityState.map { inputPriority ->
        inputPriority.isValid()
    }
    private val priorityInputValidator: (String) -> Boolean = { inputPriority ->
        inputPriority.isEmpty() || inputPriority.isValid()
    }

    override val uiScreenState: StateFlow<PickTaskPriorityScreenState> =
        combine(inputPriorityState, canConfirmFlow) { inputPriority, canConfirm ->
            PickTaskPriorityScreenState(
                inputPriority = inputPriority,
                canConfirm = canConfirm,
                priorityInputValidator = priorityInputValidator
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            PickTaskPriorityScreenState(
                inputPriority = inputPriorityState.value,
                canConfirm = inputPriorityState.value.isValid(),
                priorityInputValidator = priorityInputValidator
            )
        )

    override fun onEvent(event: PickTaskPriorityScreenEvent) {
        when (event) {
            is PickTaskPriorityScreenEvent.OnInputPriorityUpdate ->
                onInputPriorityUpdate(event)

            is PickTaskPriorityScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is PickTaskPriorityScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onInputPriorityUpdate(event: PickTaskPriorityScreenEvent.OnInputPriorityUpdate) {
        inputPriorityState.update { event.value }
    }

    private fun onConfirmClick() {
        inputPriorityState.value.let { inputPriority ->
            if (inputPriority.isValid()) {
                inputPriority.toLongOrNull()?.let { priority ->
                    setUpResult(PickTaskPriorityScreenResult.Confirm(priority))
                }
            }
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskPriorityScreenResult.Dismiss)
    }

    private fun String.isValid(): Boolean =
        this.toLongOrNull()
            ?.let { priority -> priority in availablePriorityRange && this.length <= maxPriorityLength }
            ?: false

}