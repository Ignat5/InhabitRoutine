package com.example.inhabitroutine.core.database.api.model.core

data class TaskEntity(
    public val id: String,
    public val type: String,
    public val progressType: String,
    public val title: String,
    public val description: String,
    public val startEpochDay: Long,
    public val endEpochDay: Long,
    public val createdAt: Long,
    public val deletedAt: Long?
)
