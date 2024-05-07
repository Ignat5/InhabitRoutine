package com.example.inhabitroutine.feature.search_tasks

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.common.BaseTaskDefaults
import com.example.inhabitroutine.domain.model.task.TaskModel
import com.example.inhabitroutine.feature.search_tasks.components.SearchTasksScreenEvent
import com.example.inhabitroutine.feature.search_tasks.components.SearchTasksScreenState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchTasksScreen(
    state: SearchTasksScreenState,
    onEvent: (SearchTasksScreenEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var currentTextValue by remember {
        mutableStateOf(state.searchQuery)
    }
    val canClearQuery by remember {
        derivedStateOf {
            currentTextValue.isNotBlank()
        }
    }
    BackHandler { onEvent(SearchTasksScreenEvent.OnLeaveRequest) }
    SearchBar(
        query = currentTextValue,
        onQueryChange = {
            currentTextValue = it
        },
        onSearch = {
            focusManager.clearFocus(true)
        },
        active = true,
        onActiveChange = {},
        leadingIcon = {
            IconButton(onClick = { onEvent(SearchTasksScreenEvent.OnLeaveRequest) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            if (canClearQuery) {
                IconButton(onClick = { currentTextValue = "" }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null
                    )
                }
            }
        },
        placeholder = {
            Text(text = stringResource(id = R.string.search_label))
        },
        modifier = Modifier.focusRequester(focusRequester)
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(
                items = state.allTasks,
                key = { it.id }
            ) { item ->
                ItemTask(
                    item = item,
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = BaseTaskDefaults.taskItemPlacementAnimationSpec
                    ),
                    onClick = {
                        focusManager.clearFocus(true)
                        onEvent(SearchTasksScreenEvent.OnTaskClick(item.id))
                    }
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { currentTextValue }
            .collectLatest {
                onEvent(SearchTasksScreenEvent.OnInputQueryUpdate(it))
            }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun ItemTask(
    item: TaskModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}