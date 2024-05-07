package com.ignatlegostaev.inhabitroutine.feature.view_habits.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.HabitFilterByStatus
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.HabitSort
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.ViewHabitsMessage

@Immutable
data class ViewHabitsScreenState(
    val allHabitsResult: UIResultModel<List<TaskModel.Habit>>,
    val filterByStatus: HabitFilterByStatus?,
    val sort: HabitSort,
    val message: ViewHabitsMessage
) : ScreenState
