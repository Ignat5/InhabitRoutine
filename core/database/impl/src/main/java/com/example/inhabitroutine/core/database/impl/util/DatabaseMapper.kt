package com.example.inhabitroutine.core.database.impl.util

import com.example.inhabitroutine.core.database.task.api.TaskEntity
import database.TaskTable

fun TaskTable.toTaskEntity() = TaskEntity(
    taskId = taskId,
    versionStartEpochDay = versionStartEpochDay,
    type = type,
    progressType = progressType,
    title = title,
    description = description,
    startEpochDay = startEpochDay,
    endEpochDay = endEpochDay,
    progressContent = progressContent,
    frequencyContent = frequencyContent,
    isArchived = isArchived,
    createdAt = createdAt,
    isDraft = isDraft
)

fun TaskEntity.toTaskTable() = TaskTable(
    taskId = taskId,
    versionStartEpochDay = versionStartEpochDay,
    type = type,
    progressType = progressType,
    title = title,
    description = description,
    startEpochDay = startEpochDay,
    endEpochDay = endEpochDay,
    progressContent = progressContent,
    frequencyContent = frequencyContent,
    isArchived = isArchived,
    isDraft = isDraft,
    createdAt = createdAt,
)