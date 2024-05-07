package com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_time_record.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface EnterTaskTimeRecordScreenEvent: ScreenEvent {
    data class OnInputHoursUpdate(val value: Int) : EnterTaskTimeRecordScreenEvent
    data class OnInputMinutesUpdate(val value: Int) : EnterTaskTimeRecordScreenEvent
    data object OnConfirmClick : EnterTaskTimeRecordScreenEvent
    data object OnDismissRequest : EnterTaskTimeRecordScreenEvent
}