package com.example.inhabitroutine.core.presentation.ui.util

import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType

fun TaskType.toIconId(): Int = when (this) {
    TaskType.Habit -> R.drawable.ic_habit
    TaskType.RecurringTask -> R.drawable.ic_recurring_task
    TaskType.SingleTask -> R.drawable.ic_task
}

fun TaskType.toTitleStringId(): Int = when (this) {
    TaskType.Habit -> R.string.habit_title
    TaskType.RecurringTask -> R.string.recurring_task_title
    TaskType.SingleTask -> R.string.single_task_title
}

fun TaskProgressType.toIconId(): Int = when (this) {
    TaskProgressType.YesNo -> R.drawable.ic_progress_yes_no
    TaskProgressType.Number -> R.drawable.ic_progress_number
    TaskProgressType.Time -> R.drawable.ic_progress_time
}