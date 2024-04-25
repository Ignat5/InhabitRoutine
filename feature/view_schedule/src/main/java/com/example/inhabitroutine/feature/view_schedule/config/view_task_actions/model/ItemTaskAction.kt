package com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.model

import com.example.inhabitroutine.domain.model.record.content.RecordEntry
import com.example.inhabitroutine.domain.model.task.content.TaskProgress

sealed class ItemTaskAction(val type: Type) {
    enum class Type { Progress, Done, Skip, Fail, Reset }

    data object Done : ItemTaskAction(Type.Done)
    data object Skip : ItemTaskAction(Type.Skip)
    data object Fail : ItemTaskAction(Type.Fail)
    data object Reset : ItemTaskAction(Type.Reset)

    sealed class ContinuousProgress : ItemTaskAction(Type.Progress) {
        abstract val progress: TaskProgress
        abstract val entry: RecordEntry.HabitEntry.Continuous?

        data class Number(
            override val progress: TaskProgress.Number,
            override val entry: RecordEntry.HabitEntry.Continuous.Number?
        ) : ContinuousProgress()

        data class Time(
            override val progress: TaskProgress.Time,
            override val entry: RecordEntry.HabitEntry.Continuous.Time?
        ) : ContinuousProgress()
    }
}