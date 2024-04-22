package com.example.inhabitroutine.domain.model.derived

import com.example.inhabitroutine.domain.model.record.RecordModel
import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.TaskModel
import kotlinx.datetime.LocalDate

sealed interface TaskWithRecordModel {
    val task: TaskModel
    val record: RecordModel?
    val status: TaskStatus

    sealed interface Habit : TaskWithRecordModel {
        override val task: TaskModel.Habit
        override val record: RecordModel.HabitRecord?
        override val status: TaskStatus.HabitStatus

        sealed interface HabitContinuous : Habit {
            override val task: TaskModel.Habit.HabitContinuous
            override val record: RecordModel.HabitRecord.Continuous?

            data class HabitNumber(
                override val task: TaskModel.Habit.HabitContinuous.HabitNumber,
                override val record: RecordModel.HabitRecord.Continuous.Number?,
                override val status: TaskStatus.HabitStatus
            ) : HabitContinuous

            data class HabitTime(
                override val task: TaskModel.Habit.HabitContinuous.HabitTime,
                override val record: RecordModel.HabitRecord.Continuous.Time?,
                override val status: TaskStatus.HabitStatus
            ) : HabitContinuous
        }
    }

    sealed interface Task : TaskWithRecordModel {
        override val record: RecordModel.TaskRecord?
        override val status: TaskStatus.SingleRecurringTaskStatus

        data class RecurringTask(
            override val task: TaskModel.Task.RecurringTask,
            override val record: RecordModel.TaskRecord?,
            override val status: TaskStatus.SingleRecurringTaskStatus
        ) : Task

        data class SingleTask(
            override val task: TaskModel.Task.SingleTask,
            override val record: RecordModel.TaskRecord?,
            override val status: TaskStatus.SingleRecurringTaskStatus
        ) : Task
    }
}