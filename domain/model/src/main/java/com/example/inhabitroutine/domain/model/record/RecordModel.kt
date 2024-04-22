package com.example.inhabitroutine.domain.model.record

import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import kotlinx.datetime.LocalDate

data class RecordModel(
    val id: String,
    val taskId: String,
    val entry: RecordEntry,
    val entryDate: LocalDate
)

//sealed interface RecordModel {
//    val id: String
//    val taskId: String
//    val entry: RecordEntry
//    val entryDate: LocalDate
//
//    sealed interface HabitRecord : RecordModel {
//        override val entry: RecordEntry.HabitEntry
//
//        sealed interface Continuous : HabitRecord {
//            override val entry: RecordEntry.HabitEntry.Continuous
//
//            data class Number(
//                override val id: String,
//                override val taskId: String,
//                override val entry: RecordEntry.HabitEntry.Continuous.Number,
//                override val entryDate: LocalDate
//            ) : Continuous
//
//            data class Time(
//                override val id: String,
//                override val taskId: String,
//                override val entry: RecordEntry.HabitEntry.Continuous.Time,
//                override val entryDate: LocalDate
//            ) : Continuous
//        }
//
//        data class YesNo(
//            override val id: String,
//            override val taskId: String,
//            override val entry: RecordEntry.HabitEntry.YesNo,
//            override val entryDate: LocalDate
//        ) : HabitRecord
//    }
//
//    data class TaskRecord(
//        override val id: String,
//        override val taskId: String,
//        override val entry: RecordEntry.TaskEntry,
//        override val entryDate: LocalDate
//    ) : RecordModel
//}