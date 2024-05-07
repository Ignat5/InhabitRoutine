package com.ignatlegostaev.inhabitroutine.domain.model.reminder.type

enum class ReminderType {
    NoReminder,
    Notification;

    companion object {
        val allNotifiableTypes: Set<ReminderType>
            get() = setOf(Notification)
    }
}