package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.components.result.ScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig

sealed interface BaseCreateEditTaskScreenEvent : ScreenEvent {
    data class OnItemConfigClick(
        val itemConfig: BaseItemTaskConfig
    ) : BaseCreateEditTaskScreenEvent

    sealed interface ResultEvent : BaseCreateEditTaskScreenEvent {
        val result: ScreenResult

        data class PickTaskTitle(
            override val result: PickTaskTitleScreenResult
        ) : ResultEvent

        data class PickTaskNumberProgress(
            override val result: PickTaskNumberProgressScreenResult
        ) : ResultEvent

        data class PickTaskTimeProgress(
            override val result: PickTaskTimeProgressScreenResult
        ) : ResultEvent

        data class PickTaskFrequency(
            override val result: PickTaskFrequencyScreenResult
        ) : ResultEvent

        data class PickTaskDescription(
            override val result: PickTaskDescriptionScreenResult
        ) : ResultEvent

        data class PickTaskPriority(
            override val result: PickTaskPriorityScreenResult
        ) : ResultEvent

        sealed interface PickDate : ResultEvent {
            override val result: PickDateScreenResult

            data class Date(
                override val result: PickDateScreenResult
            ) : PickDate

            data class StartDate(
                override val result: PickDateScreenResult
            ) : PickDate

            data class EndDate(
                override val result: PickDateScreenResult
            ) : PickDate
        }
    }
}