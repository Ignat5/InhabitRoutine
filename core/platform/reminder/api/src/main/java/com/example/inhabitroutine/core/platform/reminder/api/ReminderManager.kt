package com.example.inhabitroutine.core.platform.reminder.api

interface ReminderManager {
    fun setReminder(reminderId: String, millis: Long)
    fun resetReminderById(reminderId: String)
}