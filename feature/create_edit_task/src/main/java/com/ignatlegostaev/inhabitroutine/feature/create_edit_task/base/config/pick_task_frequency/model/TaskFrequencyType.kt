package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.model

import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency

enum class TaskFrequencyType {
    EveryDay,
    DaysOfWeek
}

internal val TaskFrequency.type: TaskFrequencyType
    get() = when (this) {
        is TaskFrequency.EveryDay -> TaskFrequencyType.EveryDay
        is TaskFrequency.DaysOfWeek -> TaskFrequencyType.DaysOfWeek
    }