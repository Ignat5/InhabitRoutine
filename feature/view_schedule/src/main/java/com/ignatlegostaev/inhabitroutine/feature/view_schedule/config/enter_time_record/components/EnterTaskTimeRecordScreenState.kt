package com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_time_record.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.datetime.LocalDate

data class EnterTaskTimeRecordScreenState(
    val taskModel: TaskModel.Habit.HabitContinuous.HabitTime,
    val inputHours: Int,
    val inputMinutes: Int,
    val date: LocalDate
) : ScreenState
