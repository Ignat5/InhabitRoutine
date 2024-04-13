package com.example.inhabitroutine.core.database.api.model.derived

import com.example.inhabitroutine.core.database.api.model.core.ReminderEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskContentEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskEntity

data class TaskWithContentEntity(
    val task: TaskEntity,
    val taskContent: TaskContentEntity,
    val reminder: ReminderEntity?
)
