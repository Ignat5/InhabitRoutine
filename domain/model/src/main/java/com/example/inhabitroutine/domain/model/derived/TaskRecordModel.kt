package com.example.inhabitroutine.domain.model.derived

//import com.example.inhabitroutine.domain.model.record.content.RecordEntry
//import kotlinx.datetime.LocalDate
//
//sealed interface TaskRecordModel {
//    val id: String
//    val taskId: String
//    val entry: RecordEntry
//    val date: LocalDate
//    val createdAt: Long
//
//    sealed interface Habit : TaskRecordModel {
//        override val entry: RecordEntry.HabitEntry
//
//        sealed interface HabitContinuous : Habit {
//            override val entry: RecordEntry.HabitEntry.Continuous
//
//            data class HabitNumber(
//                override val id: String,
//                override val taskId: String,
//                override val entry: RecordEntry.HabitEntry.Continuous.Number,
//                override val date: LocalDate,
//                override val createdAt: Long
//            ) : HabitContinuous
//
//            data class HabitTime(
//                override val id: String,
//                override val taskId: String,
//                override val entry: RecordEntry.HabitEntry.Continuous.Time,
//                override val date: LocalDate,
//                override val createdAt: Long
//            ) : HabitContinuous
//        }
//
//        data class HabitYesNo(
//            override val id: String,
//            override val taskId: String,
//            override val entry: RecordEntry.HabitEntry.YesNo,
//            override val date: LocalDate,
//            override val createdAt: Long
//        ) : Habit
//    }
//
//    data class Task(
//        override val id: String,
//        override val taskId: String,
//        override val entry: RecordEntry.TaskEntry,
//        override val date: LocalDate,
//        override val createdAt: Long
//    ) : TaskRecordModel
//}