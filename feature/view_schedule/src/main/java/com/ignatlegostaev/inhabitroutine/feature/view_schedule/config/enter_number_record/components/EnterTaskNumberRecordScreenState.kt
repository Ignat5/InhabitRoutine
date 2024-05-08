package com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_number_record.components

import androidx.compose.runtime.Immutable
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import kotlinx.datetime.LocalDate

@Immutable
data class EnterTaskNumberRecordScreenState(
    val taskModel: TaskModel.Habit.HabitContinuous.HabitNumber,
    val inputNumber: String,
    val canConfirm: Boolean,
    val date: LocalDate,
    val inputNumberValidator: (String) -> Boolean
) : ScreenState
