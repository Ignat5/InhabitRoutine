package com.example.inhabitroutine.core.database.api.model.core

data class TaskContentEntity(
    public val id: String,
    public val taskId: String,
    public val progress: String,
    public val frequency: String,
    public val archive: String,
    public val startEpochDay: Long,
    public val createdAt: Long
)
