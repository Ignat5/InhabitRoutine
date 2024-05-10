package com.ignatlegostaev.inhabitroutine.core.database.impl.util

import com.ignatlegostaev.inhabitroutine.core.database.impl.RecordTable
import com.ignatlegostaev.inhabitroutine.core.database.impl.ReminderTable
import com.ignatlegostaev.inhabitroutine.core.database.impl.TaskContentTable
import com.ignatlegostaev.inhabitroutine.core.database.impl.TaskTable
import com.ignatlegostaev.inhabitroutine.core.database.impl.TaskView
import com.ignatlegostaev.inhabitroutine.core.database.record.api.RecordEntity
import com.ignatlegostaev.inhabitroutine.core.database.reminder.api.ReminderEntity
import com.ignatlegostaev.inhabitroutine.core.database.task.api.TaskEntity

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
    priority = task_priority,
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
    createdAt = createdAt,
    priority = priority
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