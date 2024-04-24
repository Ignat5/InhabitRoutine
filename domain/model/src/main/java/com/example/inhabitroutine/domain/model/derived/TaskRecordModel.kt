package com.example.inhabitroutine.domain.model.derived

import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import kotlinx.datetime.LocalDate

sealed interface TaskRecordModel {
    val id: String
    val taskId: String
    val entry: RecordEntry
    val date: LocalDate
    val createdAt: Long

    sealed interface Habit : TaskRecordModel {
        override val entry: RecordEntry.HabitEntry

        sealed interface HabitContinuous {

        }
    }

    data class Task(
        override val id: String,
        override val taskId: String,
        override val entry: RecordEntry.TaskEntry,
        override val date: LocalDate,
        override val createdAt: Long
    ) : TaskRecordModel
}