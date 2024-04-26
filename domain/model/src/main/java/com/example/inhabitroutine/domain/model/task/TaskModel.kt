package com.example.inhabitroutine.domain.model.task

import com.example.inhabitroutine.domain.model.task.content.TaskDate
import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.domain.model.task.content.TaskProgress
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import kotlinx.datetime.LocalDate

sealed interface TaskModel {
    val id: String
    val type: TaskType
    val progressType: TaskProgressType
    val title: String
    val description: String
    val date: TaskDate
    val isArchived: Boolean
    val versionStartDate: LocalDate
    val isDraft: Boolean
    val createdAt: Long

    sealed interface RecurringActivity {
        val frequency: TaskFrequency
    }

    sealed class Habit(
        final override val progressType: TaskProgressType
    ) : TaskModel, RecurringActivity {
        final override val type: TaskType = TaskType.Habit
        abstract override val date: TaskDate.Period

        sealed class HabitContinuous(progressType: TaskProgressType) : Habit(progressType) {
            abstract val progress: TaskProgress

            data class HabitNumber(
                override val id: String,
                override val title: String,
                override val description: String,
                override val date: TaskDate.Period,
                override val progress: TaskProgress.Number,
                override val frequency: TaskFrequency,
                override val isArchived: Boolean,
                override val versionStartDate: LocalDate,
                override val isDraft: Boolean,
                override val createdAt: Long
            ) : HabitContinuous(TaskProgressType.Number)

            data class HabitTime(
                override val id: String,
                override val title: String,
                override val description: String,
                override val date: TaskDate.Period,
                override val progress: TaskProgress.Time,
                override val frequency: TaskFrequency,
                override val isArchived: Boolean,
                override val versionStartDate: LocalDate,
                override val isDraft: Boolean,
                override val createdAt: Long
            ) : HabitContinuous(TaskProgressType.Time)
        }

        data class HabitYesNo(
            override val id: String,
            override val title: String,
            override val description: String,
            override val date: TaskDate.Period,
            override val frequency: TaskFrequency,
            override val isArchived: Boolean,
            override val versionStartDate: LocalDate,
            override val isDraft: Boolean,
            override val createdAt: Long
        ) : Habit(TaskProgressType.YesNo)
    }

    sealed class Task(
        final override val type: TaskType
    ) : TaskModel {
        final override val progressType: TaskProgressType = TaskProgressType.YesNo

        data class RecurringTask(
            override val id: String,
            override val title: String,
            override val description: String,
            override val date: TaskDate.Period,
            override val frequency: TaskFrequency,
            override val isArchived: Boolean,
            override val versionStartDate: LocalDate,
            override val isDraft: Boolean,
            override val createdAt: Long
        ) : Task(TaskType.RecurringTask), RecurringActivity

        data class SingleTask(
            override val id: String,
            override val title: String,
            override val description: String,
            override val date: TaskDate.Day,
            override val isArchived: Boolean,
            override val versionStartDate: LocalDate,
            override val isDraft: Boolean,
            override val createdAt: Long
        ) : Task(TaskType.SingleTask)
    }
}