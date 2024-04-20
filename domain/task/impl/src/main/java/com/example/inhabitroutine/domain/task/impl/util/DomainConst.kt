package com.example.inhabitroutine.domain.task.impl.util

import com.example.inhabitroutine.domain.model.task.content.TaskFrequency
import com.example.inhabitroutine.domain.model.task.type.ProgressLimitType
import kotlinx.datetime.LocalTime

internal object DomainConst {
    const val DEFAULT_TASK_TITLE = ""
    const val DEFAULT_TASK_DESCRIPTION = ""
    const val DEFAULT_TASK_IS_ARCHIVED = false
    const val DEFAULT_TASK_IS_DRAFT = true
    val DEFAULT_TASK_FREQUENCY get() = TaskFrequency.EveryDay

    /* progress */
    const val DEFAULT_LIMIT_NUMBER: Double = 0.0
    val DEFAULT_LIMIT_TYPE get() = ProgressLimitType.AtLeast
    const val DEFAULT_LIMIT_UNIT = ""
    val DEFAULT_LIMIT_TIME get() = LocalTime(hour = 0, minute = 0, second = 0)

    const val MAX_LIMIT_NUMBER: Double = 1_000_000.0
    const val MAX_LIMIT_NUMBER_LENGTH: Int = MAX_LIMIT_NUMBER.toString().length
    const val MIN_LIMIT_NUMBER: Double = 0.0
}