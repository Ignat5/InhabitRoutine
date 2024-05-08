package com.ignatlegostaev.inhabitroutine.feature.view_reminders.model

sealed interface ViewRemindersMessage {
    data object Idle : ViewRemindersMessage
    sealed interface Message : ViewRemindersMessage {
        data object CreateReminderSuccess : Message
        data object EditReminderSuccess : Message
        data object DeleteReminderSuccess : Message
        data object FailureDueToOverlap : Message
    }
}
