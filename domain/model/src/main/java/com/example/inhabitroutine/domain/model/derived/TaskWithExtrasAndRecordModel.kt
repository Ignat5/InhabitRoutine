package com.example.inhabitroutine.domain.model.derived

import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.TaskModel

sealed interface TaskWithExtrasAndRecordModel {
    val taskWithExtrasModel: TaskWithExtrasModel
    val recordEntry: RecordEntry?
    val status: TaskStatus

    sealed interface Habit : TaskWithExtrasAndRecordModel {
        override val taskWithExtrasModel: TaskWithExtrasModel.Habit
        override val recordEntry: RecordEntry.HabitEntry?
        override val status: TaskStatus.Habit

        sealed interface HabitContinuous : Habit {
            override val taskWithExtrasModel: TaskWithExtrasModel.Habit.HabitContinuous
            override val recordEntry: RecordEntry.HabitEntry.Continuous?
            override val status: TaskStatus.Habit

            data class HabitNumber(
                override val taskWithExtrasModel: TaskWithExtrasModel.Habit.HabitContinuous.HabitNumber,
                override val recordEntry: RecordEntry.HabitEntry.Continuous.Number?,
                override val status: TaskStatus.Habit
            ) : HabitContinuous

            data class HabitTime(
                override val taskWithExtrasModel: TaskWithExtrasModel.Habit.HabitContinuous.HabitTime,
                override val recordEntry: RecordEntry.HabitEntry.Continuous.Time?,
                override val status: TaskStatus.Habit
            ) : HabitContinuous
        }

        data class HabitYesNo(
            override val taskWithExtrasModel: TaskWithExtrasModel.Habit.HabitYesNo,
            override val recordEntry: RecordEntry.HabitEntry.YesNo?,
            override val status: TaskStatus.Habit
        ) : Habit
    }

    sealed interface Task : TaskWithExtrasAndRecordModel {
        override val taskWithExtrasModel: TaskWithExtrasModel.Task
        override val recordEntry: RecordEntry.TaskEntry?
        override val status: TaskStatus.Task

        data class RecurringTask(
            override val taskWithExtrasModel: TaskWithExtrasModel.Task.RecurringTask,
            override val recordEntry: RecordEntry.TaskEntry?,
            override val status: TaskStatus.Task
        ) : Task

        data class SingleTask(
            override val taskWithExtrasModel: TaskWithExtrasModel.Task.SingleTask,
            override val recordEntry: RecordEntry.TaskEntry?,
            override val status: TaskStatus.Task
        ) : Task
    }
}