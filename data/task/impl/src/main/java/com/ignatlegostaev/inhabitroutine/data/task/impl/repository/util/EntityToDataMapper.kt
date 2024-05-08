package com.ignatlegostaev.inhabitroutine.data.task.impl.repository.util

import com.ignatlegostaev.inhabitroutine.core.database.task.api.TaskEntity
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.model.task.TaskContentDataModel
import com.ignatlegostaev.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun TaskEntity.toTaskDataModel(json: Json): TaskDataModel? {
    return TaskDataModel(
        id = taskId,
        type = type.decodeFromTaskType(json) ?: return null,
        progressType = progressType.decodeFromTaskProgressType(json) ?: return null,
        title = title,
        description = description,
        startDate = startEpochDay.toDateFromEpochDay(),
        endDate = endEpochDay.toDateFromEpochDay()
            .let { date -> if (date != distantFutureDate) date else null },
        progress = progressContent.decodeFromTaskProgress(json) ?: return null,
        frequency = frequencyContent.decodeFromTaskFrequency(json) ?: return null,
        isArchived = isArchived.decodeFromBoolean(json) ?: return null,
        versionStartDate = versionStartEpochDay.toDateFromEpochDay(),
        createdAt = createdAt,
        isDraft = isDraft.decodeFromBoolean(json) ?: return null
    )
}

internal fun TaskDataModel.toTaskEntity(json: Json): TaskEntity? {
    return TaskEntity(
        taskId = id,
        versionStartEpochDay = versionStartDate.encodeToEpochDay(),
        type = type.encodeToString(json) ?: return null,
        progressType = progressType.encodeToString(json) ?: return null,
        title = title,
        description = description,
        startEpochDay = startDate.encodeToEpochDay(),
        endEpochDay = endDate.encodeToEpochDay(),
        progressContent = progress.encodeToString(json) ?: return null,
        frequencyContent = frequency.encodeToString(json) ?: return null,
        isArchived = isArchived.encodeToString(json) ?: return null,
        createdAt = createdAt,
        isDraft = isDraft.encodeToString(json) ?: return null
    )
}


/* progress */
private fun TaskContentDataModel.ProgressContent.encodeToString(json: Json): String? =
    runCatching {
        json.encodeToString(this)
    }.getOrNull()

private fun String.decodeFromTaskProgress(json: Json): TaskContentDataModel.ProgressContent? =
    runCatching {
        json.decodeFromString<TaskContentDataModel.ProgressContent>(this)
    }.getOrNull()

/* frequency */
private fun TaskContentDataModel.FrequencyContent.encodeToString(json: Json): String? =
    runCatching {
        json.encodeToString(this)
    }.getOrNull()

private fun String.decodeFromTaskFrequency(json: Json): TaskContentDataModel.FrequencyContent? =
    runCatching {
        json.decodeFromString<TaskContentDataModel.FrequencyContent>(this)
    }.getOrNull()

/* archive */
internal fun Boolean.encodeToString(json: Json): String? =
    runCatching {
        json.encodeToString(this)
    }.getOrNull()

internal fun String.decodeFromBoolean(json: Json): Boolean? =
    runCatching {
        json.decodeFromString<Boolean>(this)
    }.getOrNull()

/* time */

internal fun LocalDate?.encodeToEpochDay(): Long =
    (this ?: distantFutureDate).toEpochDays().toLong()

private fun String.decodeFromTime(json: Json): LocalTime? =
    runCatching {
        json.decodeFromString<LocalTime>(this)
    }.getOrNull()

internal fun Long.toDateFromEpochDay(): LocalDate =
    LocalDate.fromEpochDays(this.toInt())

private val distantFutureDate: LocalDate by lazy {
    Instant.DISTANT_FUTURE.toLocalDateTime(TimeZone.currentSystemDefault()).date
}

/* task type */
private fun TaskType.encodeToString(json: Json): String? = runCatching {
    json.encodeToString(this)
}.getOrNull()

private fun String.decodeFromTaskType(json: Json): TaskType? =
    runCatching {
        json.decodeFromString<TaskType>(this)
    }.getOrNull()

internal fun Collection<TaskType>.encodeToString(json: Json): Collection<String> = runCatching {
    this.mapNotNull { it.encodeToString(json) }
}.getOrElse { emptySet() }

/* task progress type */

private fun TaskProgressType.encodeToString(json: Json): String? =
    runCatching {
        json.encodeToString(this)
    }.getOrNull()

private fun String.decodeFromTaskProgressType(json: Json): TaskProgressType? =
    runCatching {
        json.decodeFromString<TaskProgressType>(this)
    }.getOrNull()

private fun String.decodeFromReminderType(json: Json): ReminderType? =
    runCatching {
        json.decodeFromString<ReminderType>(this)
    }.getOrNull()