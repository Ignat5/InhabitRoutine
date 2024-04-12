package com.example.inhabitroutine.datasource.task.impl

import com.example.inhabitroutine.core.database.InhabitRoutineDatabase
import com.example.inhabitroutine.data.model.TaskEntity
import com.example.inhabitroutine.datasource.task.api.TaskDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DefaultTaskDataSource(
    private val db: InhabitRoutineDatabase
): TaskDataSource {

    override fun readTaskById(taskId: String): Flow<TaskEntity?> {
        return flow { emit(null) }
    }

}