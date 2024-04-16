package com.example.inhabitroutine.core.database.impl.util

import com.example.inhabitroutine.core.database.task.api.TaskEntity
import database.TaskContentTable
import database.TaskTable
import database.TaskView

internal fun TaskView.toTaskEntity() = TaskEntity(
    taskId = task_id,
    versionStartEpochDay = taskContent_versionStartEpochDay,
    type = task_type,
    progressType = task_progressType,
    title = task_title,
    description = task_description,
    startEpochDay = task_startEpochDay,
    endEpochDay = task_endEpochDay,
    progressContent = taskContent_progressContent,
    frequencyContent = taskContent_frequencyContent,
    isArchived = taskContent_isArchived,
    createdAt = task_createdAt,
    isDraft = task_isDraft
)

internal fun TaskEntity.toTaskTable() = TaskTable(
    id = taskId,
    type = type,
    progressType = progressType,
    title = title,
    description = description,
    startEpochDay = startEpochDay,
    endEpochDay = endEpochDay,
    isDraft = isDraft,
    createdAt = createdAt
)

internal fun TaskEntity.toTaskContentTable() = TaskContentTable(
    taskId = taskId,
    versionStartEpochDay = versionStartEpochDay,
    progressContent = progressContent,
    frequencyContent = frequencyContent,
    isArchived = isArchived
)

//fun TaskEntity.toTaskTable() = TaskTable(
//    taskId = taskId,
//    versionStartEpochDay = versionStartEpochDay,
//    type = type,
//    progressType = progressType,
//    title = title,
//    description = description,
//    startEpochDay = startEpochDay,
//    endEpochDay = endEpochDay,
//    progressContent = progressContent,
//    frequencyContent = frequencyContent,
//    isArchived = isArchived,
//    isDraft = isDraft,
//    createdAt = createdAt,
//)