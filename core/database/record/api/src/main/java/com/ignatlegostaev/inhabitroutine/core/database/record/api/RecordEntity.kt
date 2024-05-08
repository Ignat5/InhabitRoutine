package com.ignatlegostaev.inhabitroutine.core.database.record.api

data class RecordEntity(
    public val id: String,
    public val taskId: String,
    public val entry: String,
    public val entryEpochDay: Long,
    public val createdAt: Long
)
