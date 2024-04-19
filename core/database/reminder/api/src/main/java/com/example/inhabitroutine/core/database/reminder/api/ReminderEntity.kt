package com.example.inhabitroutine.core.database.reminder.api

data class ReminderEntity(
    val id: String,
    val taskId: String,
    val reminderType: String,
    val reminderTime: String,
    val reminderSchedule: String,
    val createdAt: Long
)
