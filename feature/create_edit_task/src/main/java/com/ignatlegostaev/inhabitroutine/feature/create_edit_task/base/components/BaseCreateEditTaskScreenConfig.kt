package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.PickDateStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.PickTaskDescriptionStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.PickTaskFrequencyStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.PickTaskNumberProgressStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.PickTaskTimeProgressStateHolder
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.PickTaskTitleStateHolder

sealed interface BaseCreateEditTaskScreenConfig : ScreenConfig {
    data class PickTaskTitle(
        val stateHolder: PickTaskTitleStateHolder
    ) : BaseCreateEditTaskScreenConfig

    data class PickTaskNumberProgress(
        val stateHolder: PickTaskNumberProgressStateHolder
    ) : BaseCreateEditTaskScreenConfig

    data class PickTaskTimeProgress(
        val stateHolder: PickTaskTimeProgressStateHolder
    ) : BaseCreateEditTaskScreenConfig

    data class PickTaskFrequency(
        val stateHolder: PickTaskFrequencyStateHolder
    ) : BaseCreateEditTaskScreenConfig

    data class PickTaskDescription(
        val stateHolder: PickTaskDescriptionStateHolder
    ) : BaseCreateEditTaskScreenConfig

    sealed interface PickDate : BaseCreateEditTaskScreenConfig {
        val stateHolder: PickDateStateHolder

        data class Date(
            override val stateHolder: PickDateStateHolder
        ) : PickDate

        data class StartDate(
            override val stateHolder: PickDateStateHolder
        ) : PickDate

        data class EndDate(
            override val stateHolder: PickDateStateHolder
        ) : PickDate

    }
}