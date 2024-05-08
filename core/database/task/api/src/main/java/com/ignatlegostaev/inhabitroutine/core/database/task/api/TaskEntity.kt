package com.ignatlegostaev.inhabitroutine.core.database.task.api

data class TaskEntity(
    public val taskId: String,
    public val versionStartEpochDay: Long,
    public val type: String,
    public val progressType: String,
    public val title: String,
    public val description: String,
    public val startEpochDay: Long,
    public val endEpochDay: Long,
    public val progressContent: String,
    public val frequencyContent: String,
    public val isArchived: String,
    public val isDraft: String,
    public val createdAt: Long,
)
