package com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.filter_habit_by_status

import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.datetime.LocalDate

interface FilterHabitsByStatusUseCase {

    operator fun invoke(
        allHabits: List<TaskModel.Habit>,
        filterType: HabitFilterByStatusType,
        targetDate: LocalDate
    ): List<TaskModel.Habit>

}