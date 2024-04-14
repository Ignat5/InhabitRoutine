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
import com.example.inhabitroutine.core.ui.dialog.pick_task_type.PickTaskTypeDialog
import com.example.inhabitroutine.core.ui.dialog.pick_task_type.PickTaskTypeScreenResult
import com.example.inhabitroutine.core.ui.shared.CreateTaskFAB
import com.example.inhabitroutine.domain.model.task.type.TaskType
import com.example.inhabitroutine.feature.view_schedule.vm.ViewScheduleViewModel
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenConfig
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenEvent
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenNavigation
import com.example.inhabitroutine.feature.view_schedule.vm.components.ViewScheduleScreenState

@Composable
fun ViewScheduleScreen(
    state: ViewScheduleScreenState,
    onEvent: (ViewScheduleScreenEvent) -> Unit,
) {
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
                onClick = {
                    onEvent(ViewScheduleScreenEvent.OnCreateTaskClick)
                }
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

@Composable
fun ViewScheduleScreenConfig(
    config: ViewScheduleScreenConfig,
    onEvent: (ViewScheduleScreenEvent) -> Unit,
) {
    when (config) {
        is ViewScheduleScreenConfig.PickTaskType -> {
            PickTaskTypeDialog(allTaskTypes = config.allTaskTypes) { result ->
                when (result) {
                    is PickTaskTypeScreenResult.Confirm -> {
                        onEvent(ViewScheduleScreenEvent.PickTaskTypeResult.Confirm(result.taskType))
                    }

                    is PickTaskTypeScreenResult.Dismiss -> {
                        onEvent(ViewScheduleScreenEvent.PickTaskTypeResult.Dismiss)
                    }
                }
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