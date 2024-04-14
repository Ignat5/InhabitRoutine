package com.example.inhabitroutine.core.ui.util

import com.example.inhabitroutine.core.ui.R
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