package com.example.inhabitroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.inhabitroutine.feature.view_schedule.TestViewScheduleScreen
import com.example.inhabitroutine.feature.view_schedule.TestViewScheduleViewModel
import com.example.inhabitroutine.ui.theme.InhabitRoutineTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InhabitRoutineTheme {
                val viewModel: TestViewScheduleViewModel = hiltViewModel()
                TestViewScheduleScreen(viewModel = viewModel)
            }
        }
    }
}