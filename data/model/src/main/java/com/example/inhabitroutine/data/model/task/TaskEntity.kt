package com.example.inhabitroutine.data.model.task

import com.example.inhabitroutine.data.model.task.content.TaskArchiveEntity
import com.example.inhabitroutine.data.model.task.content.TaskFrequencyEntity
import com.example.inhabitroutine.data.model.task.content.TaskProgressEntity
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.datetime.LocalDate

data class TaskEntity(
    val id: String,
    val type: TaskType,
    val progressType: TaskProgressType,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val progress: TaskProgressEntity,
    val frequency: TaskFrequencyEntity,
    val archive: TaskArchiveEntity,
    val versionSinceDate: LocalDate,
    val createdAt: LocalDate,
    val deletedAt: Long?
)