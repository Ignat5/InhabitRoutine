package com.example.inhabitroutine.core.database.api.model.core

data class ReminderEntity(
    public val id: String,
    public val taskId: String,
    public val time: String,
    public val type: String,
    public val schedule: String,
    public val createdAt: Long
)
