package com.example.inhabitroutine.feature.view_reminders

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
import androidx.compose.ui.res.stringResource
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenEvent
import com.example.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenState

@Composable
fun ViewRemindersScreen(
    state: ViewRemindersScreenState,
    onEvent: (ViewRemindersScreenEvent) -> Unit
) {
    BackHandler { onEvent(ViewRemindersScreenEvent.OnLeaveRequest) }
    Scaffold(
        topBar = {
            ScreenTopBar(
                onBackClick = {
                    onEvent(ViewRemindersScreenEvent.OnLeaveRequest)
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.view_reminders_screen_title))
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
            }
        }
    )
}