package com.ignatlegostaev.inhabitroutine.domain.model.derived

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel

sealed interface TaskWithExtrasModel {
    val taskModel: TaskModel
    val taskExtras: TaskExtras

    sealed interface Habit : TaskWithExtrasModel {
        override val taskModel: TaskModel.Habit
        override val taskExtras: TaskExtras.Habit

        sealed interface HabitContinuous : Habit {
            override val taskModel: TaskModel.Habit.HabitContinuous
            override val taskExtras: TaskExtras.Habit.HabitContinuous

            data class HabitNumber(
                override val taskModel: TaskModel.Habit.HabitContinuous.HabitNumber,
                override val taskExtras: TaskExtras.Habit.HabitContinuous.HabitNumber
            ) : HabitContinuous

            data class HabitTime(
                override val taskModel: TaskModel.Habit.HabitContinuous.HabitTime,
                override val taskExtras: TaskExtras.Habit.HabitContinuous.HabitTime
            ) : HabitContinuous
        }

        data class HabitYesNo(
            override val taskModel: TaskModel.Habit,
            override val taskExtras: TaskExtras.Habit.HabitYesNo
        ) : Habit

    }

    sealed interface Task : TaskWithExtrasModel {
        override val taskModel: TaskModel.Task
        override val taskExtras: TaskExtras.Task

        data class RecurringTask(
            override val taskModel: TaskModel.Task.RecurringTask,
            override val taskExtras: TaskExtras.Task.RecurringTask
        ) : Task

        data class SingleTask(
            override val taskModel: TaskModel.Task.SingleTask,
            override val taskExtras: TaskExtras.Task.SingleTask
        ) : Task
    }
}


//data class TaskWithRecordAndExtrasModel(
//    val taskWithRecordModel: TaskWithRecordModel,
//    val taskExtras: TaskExtras
//)



//data class TaskWithRecordAndExtras(
//    val taskWithRecordModel: TaskWithRecordModel,
//    val allExtras: Set<TaskExtras>
//)

//sealed interface TaskWithRecordAndExtras {
//    val taskWithRecordModel: TaskWithRecordModel
//    val extras: TaskExtras
//
//    data class TaskWithRecordAndReminders(
//        override val taskWithRecordModel: TaskWithRecordModel,
//        override val extras: TaskExtras.Reminders
//    ) : TaskWithRecordAndExtras
//}