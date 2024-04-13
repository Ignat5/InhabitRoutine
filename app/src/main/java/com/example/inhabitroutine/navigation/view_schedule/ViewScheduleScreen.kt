package com.example.inhabitroutine.navigation.view_schedule

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.inhabitroutine.feature.view_schedule.ui.ViewScheduleScreen
import com.example.inhabitroutine.navigation.dest.AppNavDest
import com.example.inhabitroutine.presentation.view_schedule.AndroidViewScheduleViewModel

fun NavGraphBuilder.viewScheduleScreen() {
    composable(AppNavDest.ViewScheduleDestination.route) {
        val aViewModel: AndroidViewScheduleViewModel = hiltViewModel()
        ViewScheduleScreen(viewModel = aViewModel.viewModel)
    }
}