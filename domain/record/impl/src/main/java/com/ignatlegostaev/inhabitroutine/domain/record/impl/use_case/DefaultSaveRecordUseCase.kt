package com.ignatlegostaev.inhabitroutine.domain.record.impl.use_case

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
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
    ): ResultModel<Unit, Throwable> {
        val entry = when (requestType) {
            is SaveRecordUseCase.RequestType.EntryNumber -> RecordEntry.Number(requestType.number)
            is SaveRecordUseCase.RequestType.EntryTime -> RecordEntry.Time(requestType.time)
            is SaveRecordUseCase.RequestType.EntryDone -> RecordEntry.Done
            is SaveRecordUseCase.RequestType.EntrySkip -> RecordEntry.Skip
            is SaveRecordUseCase.RequestType.EntryFail -> RecordEntry.Fail
        }
        return recordRepository.saveRecord(
            RecordModel(
                id = randomUUID(),
                taskId = taskId,
                entry = entry,
                date = date,
                createdAt = Clock.System.now().toEpochMilliseconds()
            )
        )
    }

}