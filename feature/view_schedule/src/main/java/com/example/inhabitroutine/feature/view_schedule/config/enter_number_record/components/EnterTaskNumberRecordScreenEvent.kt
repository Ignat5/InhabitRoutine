package com.example.inhabitroutine.feature.view_schedule.config.enter_number_record.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent

sealed interface EnterTaskNumberRecordScreenEvent : ScreenEvent {

    data class OnInputNumberUpdate(
        val value: String
    ) : EnterTaskNumberRecordScreenEvent

    data object OnConfirmClick : EnterTaskNumberRecordScreenEvent
    data object OnDismissRequest : EnterTaskNumberRecordScreenEvent

}