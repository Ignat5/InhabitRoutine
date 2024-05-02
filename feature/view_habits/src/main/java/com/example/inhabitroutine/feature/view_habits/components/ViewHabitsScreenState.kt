package com.example.inhabitroutine.feature.view_habits.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.core.presentation.model.UIResultModel
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.view_habits.model.HabitFilterByStatus
import com.example.inhabitroutine.feature.view_habits.model.HabitSort
import com.example.inhabitroutine.feature.view_habits.model.ViewHabitsMessage

@Immutable
data class ViewHabitsScreenState(
    val allHabitsResult: UIResultModel<List<TaskModel.Habit>>,
    val filterByStatus: HabitFilterByStatus?,
    val sort: HabitSort,
    val message: ViewHabitsMessage
) : ScreenState
