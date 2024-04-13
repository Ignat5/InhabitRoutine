package com.example.inhabitroutine.core.database.impl.util

import com.example.inhabitroutine.core.database.api.model.core.ReminderEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskContentEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskEntity
import com.example.inhabitroutine.core.database.api.model.derived.TaskWithContentEntity
import database.TaskWithContentView

fun TaskWithContentView.toTaskWithContentEntity() = TaskWithContentEntity(
    task = TaskEntity(
        id = task_id,
        type = task_type,
        progressType = task_progressType,
        title = task_title,
        description = task_description,
        startEpochDay = task_startEpochDay,
        endEpochDay = task_endEpochDay,
        createdAt = task_createdAt,
        deletedAt = task_deletedAt
    ),
    taskContent = TaskContentEntity(
        id = taskContent_id,
        taskId = taskContent_taskId,
        progress = taskContent_progress,
        frequency = taskContent_frequency,
        archive = taskContent_archive,
        startEpochDay = taskContent_startEpochDay,
        createdAt = taskContent_createdAt
    ),
    reminder = run {
        ReminderEntity(
            id = reminder_id ?: return@run null,
            taskId = reminder_taskId ?: return@run null,
            time = reminder_time ?: return@run null,
            type = reminder_type ?: return@run null,
            schedule = reminder_schedule ?: return@run null,
            createdAt = reminder_createdAt ?: return@run null
        )
    }
)