package com.ignatlegostaev.inhabitroutine.domain.record.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.mapSuccess
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.record.api.RecordRepository
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.content.RecordEntry
import com.ignatlegostaev.inhabitroutine.domain.record.api.SaveRecordUseCase
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

internal class DefaultSaveRecordUseCase(
    private val recordRepository: RecordRepository
) : SaveRecordUseCase {

    override suspend operator fun invoke(
        taskId: String,
        date: LocalDate,
        requestType: SaveRecordUseCase.RequestType
    ): ResultModel<String, Throwable> {
        val entry = requestType.mapToRecordEntry()
        val record = buildRecord(taskId, date, entry)
        val result = recordRepository.saveRecord(record)
        return result.mapSuccess { record.id }
    }

    private fun SaveRecordUseCase.RequestType.mapToRecordEntry(): RecordEntry {
        return when (this) {
            is SaveRecordUseCase.RequestType.EntryNumber -> RecordEntry.Number(this.number)
            is SaveRecordUseCase.RequestType.EntryTime -> RecordEntry.Time(this.time)
            is SaveRecordUseCase.RequestType.EntryDone -> RecordEntry.Done
            is SaveRecordUseCase.RequestType.EntrySkip -> RecordEntry.Skip
            is SaveRecordUseCase.RequestType.EntryFail -> RecordEntry.Fail
        }
    }

    private fun buildRecord(taskId: String, date: LocalDate, entry: RecordEntry): RecordModel {
        return RecordModel(
            id = randomUUID(),
            taskId = taskId,
            entry = entry,
            date = date,
            createdAt = Clock.System.now().toEpochMilliseconds()
        )
    }

}