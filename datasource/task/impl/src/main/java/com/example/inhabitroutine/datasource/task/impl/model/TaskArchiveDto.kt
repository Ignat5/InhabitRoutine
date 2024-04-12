package com.example.inhabitroutine.datasource.task.impl.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("TaskArchive")
data class TaskArchiveDto(
    val isArchived: Boolean
)
