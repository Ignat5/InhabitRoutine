package com.example.inhabitroutine.domain.model.record

import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import kotlinx.datetime.LocalDate

data class RecordModel(
    val id: String,
    val taskId: String,
    val entry: RecordEntry,
    val entryDate: LocalDate
)
