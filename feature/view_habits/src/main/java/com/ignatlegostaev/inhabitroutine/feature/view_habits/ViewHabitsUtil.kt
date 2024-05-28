package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.core.util.todayDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate

internal fun List<TaskModel.Habit>.filterHabitsByOnlyActive(
    targetDate: LocalDate = Clock.System.todayDate
) = this.let { allHabits ->
    allHabits.filter { taskModel ->
        if (!taskModel.isArchived) {
            taskModel.date.endDate?.let { endDate ->
                targetDate in taskModel.date.startDate..endDate
            } ?: (targetDate >= taskModel.date.startDate)
        } else false
    }
}

internal fun List<TaskModel.Habit>.filterHabitsByOnlyArchived() = this.let { allHabits ->
    allHabits.filter { it.isArchived }
}