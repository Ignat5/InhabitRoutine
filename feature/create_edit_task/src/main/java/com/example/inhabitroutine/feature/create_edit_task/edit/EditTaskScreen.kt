package com.example.inhabitroutine.feature.create_edit_task.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.create_edit_task.base.BaseCreateEditTaskConfig
import com.example.inhabitroutine.feature.create_edit_task.base.baseConfigItems
import com.example.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenConfig
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenEvent
import com.example.inhabitroutine.feature.create_edit_task.edit.components.EditTaskScreenState

@Composable
fun EditTaskScreen(
    state: EditTaskScreenState,
    onEvent: (EditTaskScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                taskTitle = state.taskModel?.title,
                onBack = {
                    onEvent(EditTaskScreenEvent.OnBackRequest)
                }
            )
        }
    ) {
        BackHandler { onEvent(EditTaskScreenEvent.OnBackRequest) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                baseConfigItems(
                    allTaskConfigItems = state.allTaskConfigItems,
                    onItemClick = { item ->
                        onEvent(
                            EditTaskScreenEvent.Base(
                                BaseCreateEditTaskScreenEvent.OnItemConfigClick(item)
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun EditTaskConfig(
    config: EditTaskScreenConfig,
    onEvent: (EditTaskScreenEvent) -> Unit
) {
    when (config) {
        is EditTaskScreenConfig.Base -> {
            BaseCreateEditTaskConfig(config = config.baseConfig) {
                onEvent(EditTaskScreenEvent.Base(it))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    taskTitle: String?,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = taskTitle ?: "")
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.ic_more), contentDescription = null)
            }
        }
    )
}