package com.example.inhabitroutine.core.database.api.db

import com.example.inhabitroutine.core.database.api.model.derived.TaskWithContentEntity
import kotlinx.coroutines.flow.Flow

interface TaskDao {
    fun readTaskWithContentById(taskId: String): Flow<TaskWithContentEntity?>
}