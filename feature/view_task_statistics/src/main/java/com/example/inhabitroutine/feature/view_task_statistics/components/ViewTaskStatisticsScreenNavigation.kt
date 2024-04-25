package com.example.inhabitroutine.feature.view_task_statistics.components

import com.example.inhabitroutine.core.presentation.components.navigation.ScreenNavigation

sealed interface ViewTaskStatisticsScreenNavigation : ScreenNavigation {
    data object Back : ViewTaskStatisticsScreenNavigation
}