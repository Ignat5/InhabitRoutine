package com.example.inhabitroutine.datasource.task.impl.model

import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.datetime.LocalDate

data class TaskDto(
    val id: String,
    val type: TaskType,
    val progressType: TaskProgressType,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val progress: TaskProgressDto,
    val frequency: TaskFrequencyDto,
    val archive: TaskArchiveDto,
    val versionSinceDate: LocalDate,
    val createdAt: Long,
    val deletedAt: Long?
)
