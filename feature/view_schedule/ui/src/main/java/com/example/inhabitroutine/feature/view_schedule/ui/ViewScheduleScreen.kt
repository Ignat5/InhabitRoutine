package com.example.inhabitroutine.feature.view_schedule.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.inhabitroutine.core.ui.R
import com.example.inhabitroutine.core.ui.base.BaseScreen
import com.example.inhabitroutine.core.ui.shared.CreateTaskFAB
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
        configContent = { config, onEvent -> }
    ) { state, onEvent ->
        Scaffold(
            topBar = {
                ScreenTopBar(
                    onMenuClick = {},
                    onSearchClick = {},
                    onPickDateClick = {}
                )
            },
            floatingActionButton = {
                CreateTaskFAB(
                    onClick = {}
                )
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onPickDateClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Apr 14, 2024")
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_menu),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null
                )
            }
            IconButton(onClick = onPickDateClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pick_date),
                    contentDescription = null
                )
            }
        }
    )
}