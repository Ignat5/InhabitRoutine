package com.example.inhabitroutine.feature.view_schedule.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inhabitroutine.feature.view_schedule.vm.ViewScheduleViewModel

@Composable
fun ViewScheduleScreen(viewModel: ViewScheduleViewModel) {
    val state = viewModel.uiScreenState.collectAsStateWithLifecycle()
    Text(text = "ViewScheduleScreen: testString: ${viewModel}")
}