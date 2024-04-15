package com.example.inhabitroutine.data.task.impl.repository.model.task

import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.datetime.LocalDate

data class TaskDataModel(
    val id: String,
    val type: TaskType,
    val progressType: TaskProgressType,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val progress: TaskContentDataModel.ProgressContent,
    val frequency: TaskContentDataModel.FrequencyContent,
    val isArchived: Boolean,
    val isDraft: Boolean,
    val versionStartDate: LocalDate,
    val createdAt: Long,
)
