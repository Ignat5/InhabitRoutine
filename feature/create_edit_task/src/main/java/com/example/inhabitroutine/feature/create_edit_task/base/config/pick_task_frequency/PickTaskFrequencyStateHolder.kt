package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenState
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.model.TaskFrequencyType
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.model.type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek

class PickTaskFrequencyStateHolder(
    initTaskFrequency: TaskFrequency,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickTaskFrequencyScreenEvent, PickTaskFrequencyScreenState, PickTaskFrequencyScreenResult>() {

    private val inputTaskFrequencyState = MutableStateFlow(initTaskFrequency)
    private val canConfirmState = inputTaskFrequencyState.map { taskFrequency ->
        when (taskFrequency) {
            is TaskFrequency.EveryDay -> true
            is TaskFrequency.DaysOfWeek -> taskFrequency.daysOfWeek.isNotEmpty()
        }
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        false
    )

    override val uiScreenState: StateFlow<PickTaskFrequencyScreenState> =
        combine(inputTaskFrequencyState, canConfirmState) { inputTaskFrequency, canConfirm ->
            PickTaskFrequencyScreenState(
                inputTaskFrequency = inputTaskFrequency,
                canConfirm = canConfirm
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            PickTaskFrequencyScreenState(
                inputTaskFrequency = inputTaskFrequencyState.value,
                canConfirm = canConfirmState.value
            )
        )

    override fun onEvent(event: PickTaskFrequencyScreenEvent) {
        when (event) {
            is PickTaskFrequencyScreenEvent.OnFrequencyTypeClick ->
                onFrequencyTypeClick(event)

            is PickTaskFrequencyScreenEvent.OnDayOfWeekClick ->
                onDayOfWeekClick(event)

            is PickTaskFrequencyScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is PickTaskFrequencyScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onFrequencyTypeClick(event: PickTaskFrequencyScreenEvent.OnFrequencyTypeClick) {
        event.type.let { clickedType ->
            inputTaskFrequencyState.value.type.let { currentType ->
                if (clickedType != currentType) {
                    inputTaskFrequencyState.update {
                        when (clickedType) {
                            TaskFrequencyType.EveryDay -> TaskFrequency.EveryDay
                            TaskFrequencyType.DaysOfWeek -> TaskFrequency.DaysOfWeek(emptySet())
                        }
                    }
                }
            }
        }
    }

    private fun onDayOfWeekClick(event: PickTaskFrequencyScreenEvent.OnDayOfWeekClick) {
        event.dayOfWeek.let { clickedDayOfWeek ->
            inputTaskFrequencyState.update { old ->
                (old as? TaskFrequency.DaysOfWeek)?.daysOfWeek?.let { oldSet ->
                    val newSet = mutableSetOf<DayOfWeek>()
                    newSet.addAll(oldSet)
                    if (newSet.contains(clickedDayOfWeek)) newSet.remove(clickedDayOfWeek)
                    else newSet.add(clickedDayOfWeek)
                    TaskFrequency.DaysOfWeek(newSet)
                } ?: old
            }
        }
    }

    private fun onConfirmClick() {
        if (canConfirmState.value) {
            val frequency = when (val f = inputTaskFrequencyState.value) {
                is TaskFrequency.EveryDay -> TaskFrequency.EveryDay
                is TaskFrequency.DaysOfWeek -> {
                    if (f.daysOfWeek.size != DayOfWeek.entries.size) {
                        TaskFrequency.DaysOfWeek(f.daysOfWeek)
                    } else TaskFrequency.EveryDay
                }
            }
            setUpResult(PickTaskFrequencyScreenResult.Confirm(frequency))
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickTaskFrequencyScreenResult.Dismiss)
    }

}