package com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.model.TaskFrequencyType
import kotlinx.datetime.DayOfWeek

sealed interface PickTaskFrequencyScreenEvent : ScreenEvent {
    data class OnFrequencyTypeClick(val type: TaskFrequencyType) : PickTaskFrequencyScreenEvent
    data class OnDayOfWeekClick(val dayOfWeek: DayOfWeek) : PickTaskFrequencyScreenEvent
    data object OnConfirmClick : PickTaskFrequencyScreenEvent
    data object OnDismissRequest : PickTaskFrequencyScreenEvent
}