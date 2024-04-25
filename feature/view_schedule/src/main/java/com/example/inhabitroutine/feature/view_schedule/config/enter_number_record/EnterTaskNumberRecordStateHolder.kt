package com.example.inhabitroutine.feature.view_schedule.config.enter_number_record

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.core.presentation.ui.util.limitNumberToDisplay
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.domain.task.api.use_case.ValidateProgressLimitNumberUseCase
import com.example.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenEvent
import com.example.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenResult
import com.example.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class EnterTaskNumberRecordStateHolder(
    private val taskModel: TaskModel.Habit.HabitContinuous.HabitNumber,
    entry: RecordEntry.HabitEntry.Continuous.Number?,
    private val date: LocalDate,
    private val validateProgressLimitNumberUseCase: ValidateProgressLimitNumberUseCase,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<EnterTaskNumberRecordScreenEvent, EnterTaskNumberRecordScreenState, EnterTaskNumberRecordScreenResult>() {

    private val inputNumberState = MutableStateFlow(
        (entry as? RecordEntry.Number)?.number?.limitNumberToDisplay() ?: ""
    )

    private val inputNumberValidator: (String) -> Boolean = { input ->
        input.isEmpty() || input.isValid()
    }

    private val canConfirmState = inputNumberState.map { input ->
        input.isValid()
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        false
    )

    override val uiScreenState: StateFlow<EnterTaskNumberRecordScreenState> =
        combine(inputNumberState, canConfirmState) { inputNumber, canConfirm ->
            EnterTaskNumberRecordScreenState(
                taskModel = taskModel,
                inputNumber = inputNumber,
                canConfirm = canConfirm,
                date = date,
                inputNumberValidator = inputNumberValidator
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            EnterTaskNumberRecordScreenState(
                taskModel = taskModel,
                inputNumber = inputNumberState.value,
                canConfirm = canConfirmState.value,
                date = date,
                inputNumberValidator = inputNumberValidator
            )
        )

    override fun onEvent(event: EnterTaskNumberRecordScreenEvent) {
        when (event) {
            is EnterTaskNumberRecordScreenEvent.OnInputNumberUpdate ->
                onInputNumberUpdate(event)

            is EnterTaskNumberRecordScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is EnterTaskNumberRecordScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onInputNumberUpdate(event: EnterTaskNumberRecordScreenEvent.OnInputNumberUpdate) {
        inputNumberState.update { event.value }
    }

    private fun onConfirmClick() {
        if (canConfirmState.value) {
            inputNumberState.value.toDoubleOrNull()?.let { number ->
                setUpResult(
                    EnterTaskNumberRecordScreenResult.Confirm(
                        taskId = taskModel.id,
                        date = date,
                        number = number
                    )
                )
            }
        }
    }

    private fun onDismissRequest() {
        setUpResult(EnterTaskNumberRecordScreenResult.Dismiss)
    }

    private fun String.isValid(): Boolean = this.let { input ->
        input.toDoubleOrNull()?.let { validateProgressLimitNumberUseCase(it) } ?: false
    }

}