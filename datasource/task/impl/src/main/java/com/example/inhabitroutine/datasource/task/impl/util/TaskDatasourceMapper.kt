package com.example.inhabitroutine.datasource.task.impl.util

//import com.example.inhabitroutine.data.model.task.TaskEntity
//import com.example.inhabitroutine.domain.model.task.type.TaskType
//import database.TaskWithContentView
//import kotlinx.serialization.json.Json
//
//fun TaskWithContentView.toTaskEntity(json: Json): TaskEntity? {
//    return TaskEntity(
//        id = task_id,
//        type = task_type.decodeTaskType(json) ?: return null,
//        progressType =,
//        title =,
//        description =,
//        startDate =,
//        endDate =,
//        progress =,
//        frequency =,
//        archive =,
//        versionSinceDate =,
//        createdAt =,
//        deletedAt =
//    )
//}
//
//private fun String.decodeTaskType(json: Json): TaskType? = runCatching {
//    json.decodeFromString<TaskType>(this)
//}.getOrNull()
