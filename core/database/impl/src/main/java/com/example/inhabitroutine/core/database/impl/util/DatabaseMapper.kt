package com.example.inhabitroutine.core.database.impl.util

import com.example.inhabitroutine.core.database.record.api.RecordEntity
import com.example.inhabitroutine.core.database.reminder.api.ReminderEntity
import com.example.inhabitroutine.core.database.task.api.TaskEntity
import database.RecordTable
import database.ReminderTable
import database.TaskContentTable
import database.TaskTable
import database.TaskView

/* task */

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

/* reminder */

internal fun ReminderTable.toReminderEntity() = ReminderEntity(
    id = id,
    taskId = taskId,
    type = type,
    time = time,
    schedule = schedule,
    createdAt = createdAt
)

internal fun ReminderEntity.toReminderTable() = ReminderTable(
    id = id,
    taskId = taskId,
    time = time,
    type = type,
    schedule = schedule,
    createdAt = createdAt
)

/* record */
internal fun RecordTable.toRecordEntity() = RecordEntity(
    id = id,
    taskId = taskId,
    entry = entry,
    entryEpochDay = entryEpochDay,
    createdAt = createdAt
)

internal fun RecordEntity.toRecordTable() = RecordTable(
    id = id,
    taskId = taskId,
    entry = entry,
    entryEpochDay = entryEpochDay,
    createdAt = createdAt
)