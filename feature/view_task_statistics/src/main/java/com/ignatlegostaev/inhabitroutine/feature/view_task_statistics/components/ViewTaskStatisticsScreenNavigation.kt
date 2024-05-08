package com.ignatlegostaev.inhabitroutine.feature.view_task_statistics.components

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface ViewTaskStatisticsScreenNavigation : ScreenNavigation {
    data object Back : ViewTaskStatisticsScreenNavigation
}