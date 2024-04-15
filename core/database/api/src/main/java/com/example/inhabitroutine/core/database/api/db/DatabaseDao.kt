package com.example.inhabitroutine.core.database.api.db

import com.example.inhabitroutine.core.database.api.model.core.ReminderEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskContentEntity
import com.example.inhabitroutine.core.database.api.model.core.TaskEntity
import com.example.inhabitroutine.core.database.api.model.derived.TaskWithContentEntity
import com.example.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.flow.Flow

interface DatabaseDao {
    fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?>
    suspend fun saveTaskWithContent(
        taskEntity: TaskEntity,
        taskContentEntity: TaskContentEntity,
        reminderEntity: ReminderEntity?
    ): ResultModel<Unit, Throwable>
}