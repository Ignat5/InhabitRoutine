package com.example.inhabitroutine.feature.view_tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenConfig
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenEvent
import com.example.inhabitroutine.feature.view_tasks.components.ViewTasksScreenState
import com.example.inhabitroutine.feature.view_tasks.model.TaskSort

@Composable
fun ViewTasksScreen(
    state: ViewTasksScreenState,
    onEvent: (ViewTasksScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                onMenuClick = {},
                onSearchClick = {}
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                FilterSortChipRow(sort = state.sort)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun FilterSortChipRow(
    sort: TaskSort,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            SortChip(
                sort = sort
            )
        }
    }
}

@Composable
private fun SortChip(
    sort: TaskSort
) {
    FilterChip(
        selected = false,
        onClick = { /*TODO*/ },
        label = {
            Icon(
                painter = painterResource(id = R.drawable.ic_sort),
                contentDescription = null
            )
        },
    )
}

@Composable
fun ViewTasksConfig(
    config: ViewTasksScreenConfig,
    onEvent: (ViewTasksScreenEvent) -> Unit
) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.view_tasks_screen_title))
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
        }
    )
}