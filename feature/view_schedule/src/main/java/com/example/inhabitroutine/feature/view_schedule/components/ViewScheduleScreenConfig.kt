package com.example.inhabitroutine.feature.view_schedule.components

import com.example.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.PickDateStateHolder
import com.example.inhabitroutine.domain.model.task.type.TaskProgressType
import com.example.inhabitroutine.domain.model.task.type.TaskType
import com.example.inhabitroutine.feature.view_schedule.config.enter_number_record.EnterTaskNumberRecordStateHolder
import com.example.inhabitroutine.feature.view_schedule.config.enter_time_record.EnterTaskTimeRecordStateHolder
import com.example.inhabitroutine.feature.view_schedule.config.view_task_actions.ViewTaskActionsStateHolder

sealed interface ViewScheduleScreenConfig : ScreenConfig {
    data class PickTaskType(val allTaskTypes: List<TaskType>) : ViewScheduleScreenConfig
    data class PickTaskProgressType(
        val allProgressTypes: List<TaskProgressType>
    ) : ViewScheduleScreenConfig

    data class EnterTaskNumberRecord(
        val stateHolder: EnterTaskNumberRecordStateHolder
    ): ViewScheduleScreenConfig

    data class EnterTaskTimeRecord(
        val stateHolder: EnterTaskTimeRecordStateHolder
    ) : ViewScheduleScreenConfig

    data class ViewTaskActions(
        val stateHolder: ViewTaskActionsStateHolder
    ) : ViewScheduleScreenConfig

    data class PickDate(
        val stateHolder: PickDateStateHolder
    ) : ViewScheduleScreenConfig
}