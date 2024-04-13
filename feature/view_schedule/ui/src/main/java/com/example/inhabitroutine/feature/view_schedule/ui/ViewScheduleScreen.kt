package com.example.inhabitroutine.feature.view_schedule.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inhabitroutine.core.ui.BaseScreen
import com.example.inhabitroutine.feature.view_schedule.vm.ViewScheduleViewModel
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenNavigation

@Composable
fun ViewScheduleScreen(
    viewModel: ViewScheduleViewModel,
    onNavigation: (ViewScheduleScreenNavigation) -> Unit
) {
    BaseScreen(
        viewModel = viewModel,
        onNavigation = onNavigation,
        configContent = { config, onEvent ->  }
    ) { state, onEvent ->
        Text(text = "ViewScheduleScreen: testString: ${state.testString}")
    }
}