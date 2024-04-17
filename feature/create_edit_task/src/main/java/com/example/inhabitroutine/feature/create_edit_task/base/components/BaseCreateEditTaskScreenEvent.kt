package com.example.inhabitroutine.feature.create_edit_task.base.components

import com.example.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.example.inhabitroutine.core.presentation.components.result.ScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenResult
import com.example.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig

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
    }
}