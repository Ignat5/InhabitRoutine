package com.example.inhabitroutine.data.task.impl.repository.model

import com.example.inhabitroutine.data.task.impl.repository.model.reminder.ReminderEntity
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
    val progress: TaskContentEntity.ProgressContent,
    val frequency: TaskContentEntity.FrequencyContent,
    val archive: TaskContentEntity.ArchiveContent,
    val reminder: ReminderEntity?,
    val versionSinceDate: LocalDate,
    val createdAt: Long,
    val deletedAt: Long?
)
