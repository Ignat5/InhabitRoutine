package com.example.inhabitroutine.feature.view_schedule.components

import androidx.compose.runtime.Immutable
import com.example.inhabitroutine.core.presentation.components.state.ScreenState
import com.example.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import kotlinx.datetime.LocalDate

@Immutable
data class ViewScheduleScreenState(
    val currentDate: LocalDate,
    val allTasks: List<TaskWithExtrasAndRecordModel>,
    val startOfWeekDate: LocalDate,
    val todayDate: LocalDate
): ScreenState