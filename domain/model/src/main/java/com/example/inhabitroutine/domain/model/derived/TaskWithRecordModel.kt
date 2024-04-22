package com.example.inhabitroutine.domain.model.derived

//import com.example.inhabitroutine.domain.model.record.content.RecordEntry
//import com.example.inhabitroutine.domain.model.task.TaskModel
//
//sealed interface TaskWithRecordModel {
//    val task: TaskModel
//    val entry: RecordEntry?
//    val status: TaskStatus
//
//    sealed interface Habit : TaskWithRecordModel {
//        override val task: TaskModel.Habit
//        override val entry: RecordEntry.HabitEntry?
//        override val status: TaskStatus.Habit
//
//        sealed interface HabitContinuous : Habit {
//            override val task: TaskModel.Habit.HabitContinuous
//            override val entry: RecordEntry.HabitEntry.Continuous?
//
//            data class HabitNumber(
//                override val task: TaskModel.Habit.HabitContinuous.HabitNumber,
//                override val entry: RecordEntry.HabitEntry.Continuous.Number?,
//                override val status: TaskStatus.Habit
//            ) : HabitContinuous
//
//            data class HabitTime(
//                override val task: TaskModel.Habit.HabitContinuous.HabitTime,
//                override val entry: RecordEntry.HabitEntry.Continuous.Time?,
//                override val status: TaskStatus.Habit
//            ) : HabitContinuous
//        }
//
//        data class HabitYesNo(
//            override val task: TaskModel.Habit.HabitYesNo,
//            override val entry: RecordEntry.HabitEntry.YesNo?,
//            override val status: TaskStatus.Habit
//        ) : Habit
//    }
//
//    sealed interface Task : TaskWithRecordModel {
//        override val entry: RecordEntry.TaskEntry?
//        override val status: TaskStatus.Task
//
//        data class RecurringTask(
//            override val task: TaskModel.Task.RecurringTask,
//            override val entry: RecordEntry.TaskEntry?,
//            override val status: TaskStatus.Task
//        ) : Task
//
//        data class SingleTask(
//            override val task: TaskModel.Task.SingleTask,
//            override val entry: RecordEntry.TaskEntry?,
//            override val status: TaskStatus.Task
//        ) : Task
//    }
//}