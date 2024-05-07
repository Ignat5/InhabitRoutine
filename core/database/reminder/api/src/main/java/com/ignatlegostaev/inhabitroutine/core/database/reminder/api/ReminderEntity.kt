package com.ignatlegostaev.inhabitroutine.core.database.reminder.api

data class ReminderEntity(
    val id: String,
    val taskId: String,
    val type: String,
    val time: String,
    val schedule: String,
    val createdAt: Long
)
