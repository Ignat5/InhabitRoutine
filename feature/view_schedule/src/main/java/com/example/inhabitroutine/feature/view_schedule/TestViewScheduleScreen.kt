package com.example.inhabitroutine.feature.view_schedule

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TestViewScheduleScreen(
    viewModel: TestViewScheduleViewModel
) {
   Text(text = "TestViewScheduleScreen: viewModel: ${viewModel}")
}