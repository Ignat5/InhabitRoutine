package com.ignatlegostaev.inhabitroutine.data.record.impl.util

import com.ignatlegostaev.inhabitroutine.data.record.impl.model.RecordContentDataModel
import com.ignatlegostaev.inhabitroutine.data.record.impl.model.RecordDataModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.content.RecordEntry

internal fun RecordDataModel.toRecordModel(): RecordModel {
    return RecordModel(
        id = id,
        taskId = taskId,
        entry = entry.toRecordEntry(),
        date = date,
        createdAt = createdAt
    )
}

internal fun RecordModel.toRecordDataModel(): RecordDataModel {
    return RecordDataModel(
        id = id,
        taskId = taskId,
        entry = entry.toRecordEntryContent(),
        date = date,
        createdAt = createdAt
    )
}

private fun RecordContentDataModel.EntryContent.toRecordEntry() =
    when (this) {
        is RecordContentDataModel.EntryContent.Done -> RecordEntry.Done
        is RecordContentDataModel.EntryContent.Number -> RecordEntry.Number(this.number)
        is RecordContentDataModel.EntryContent.Time -> RecordEntry.Time(this.time)
        is RecordContentDataModel.EntryContent.Skip -> RecordEntry.Skip
        is RecordContentDataModel.EntryContent.Fail -> RecordEntry.Fail
    }

private fun RecordEntry.toRecordEntryContent() =
    when (this) {
        is RecordEntry.Done -> RecordContentDataModel.EntryContent.Done
        is RecordEntry.Number -> RecordContentDataModel.EntryContent.Number(this.number)
        is RecordEntry.Time -> RecordContentDataModel.EntryContent.Time(this.time)
        is RecordEntry.Skip -> RecordContentDataModel.EntryContent.Skip
        is RecordEntry.Fail -> RecordContentDataModel.EntryContent.Fail
    }