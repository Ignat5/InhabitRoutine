package com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.filter_habit_by_status.FilterHabitsByStatusUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.filter_habit_by_status.HabitFilterByStatusType
import kotlinx.datetime.LocalDate

internal class DefaultFilterHabitsByStatusUseCase : FilterHabitsByStatusUseCase {

    override fun invoke(
        allHabits: List<TaskModel.Habit>,
        filterType: HabitFilterByStatusType,
        targetDate: LocalDate
    ): List<TaskModel.Habit> {
        return when (filterType) {
            HabitFilterByStatusType.OnlyActive -> {
                allHabits.filter { habit ->
                    if (!habit.isArchived) {
                        habit.date.endDate?.let { endDate ->
                            targetDate in habit.date.startDate..endDate
                        } ?: (targetDate >= habit.date.startDate)
                    } else false
                }
            }

            HabitFilterByStatusType.OnlyArchived -> {
                allHabits.filter { it.isArchived }
            }
        }
    }

}