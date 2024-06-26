package com.ignatlegostaev.inhabitroutine.data.record.impl.util

import com.ignatlegostaev.inhabitroutine.core.database.record.api.RecordEntity
import com.ignatlegostaev.inhabitroutine.data.record.impl.model.RecordContentDataModel
import com.ignatlegostaev.inhabitroutine.data.record.impl.model.RecordDataModel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal fun RecordEntity.toRecordDataModel(json: Json): RecordDataModel? {
    return RecordDataModel(
        id = id,
        taskId = taskId,
        entry = entry.decodeFromEntry(json) ?: return null,
        date = entryEpochDay.toDateFromEpochDay(),
        createdAt = createdAt
    )
}

internal fun RecordDataModel.toRecordEntity(json: Json): RecordEntity? {
    return RecordEntity(
        id = id,
        taskId = taskId,
        entry = entry.encodeToString(json) ?: return null,
        entryEpochDay = date.encodeToEpochDay(),
        createdAt = createdAt
    )
}

/* date */
internal fun LocalDate.encodeToEpochDay() = this.toEpochDays().toLong()

internal fun Long.toDateFromEpochDay() = LocalDate.fromEpochDays(this.toInt())

/* entry */
internal fun RecordContentDataModel.EntryContent.encodeToString(json: Json) = runCatching {
    json.encodeToString(this)
}.getOrNull()

internal fun String.decodeFromEntry(json: Json): RecordContentDataModel.EntryContent? =
    runCatching {
        json.decodeFromString<RecordContentDataModel.EntryContent>(this)
    }.getOrNull()

internal val distantFutureDate: LocalDate
    get() = Instant.DISTANT_FUTURE.toLocalDateTime(TimeZone.currentSystemDefault()).date