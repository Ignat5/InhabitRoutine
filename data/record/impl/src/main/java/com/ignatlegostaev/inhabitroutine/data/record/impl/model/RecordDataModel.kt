package com.ignatlegostaev.inhabitroutine.data.record.impl.model

import kotlinx.datetime.LocalDate

data class RecordDataModel(
    val id: String,
    val taskId: String,
    val entry: RecordContentDataModel.EntryContent,
    val date: LocalDate,
    val createdAt: Long
)
