package com.ignatlegostaev.inhabitroutine.core.presentation.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private const val VISIBLE_ITEMS_COUNT = 3
private val targetHeight = TextFieldDefaults.MinHeight * 2
private val itemHeight = targetHeight / VISIBLE_ITEMS_COUNT
private const val DEFAULT_DEBOUNCE_MILLIS = 100L

@Composable
fun BaseTimePicker(
    initHours: Int,
    initMinutes: Int,
    onHoursUpdate: (hours: Int) -> Unit,
    onMinutesUpdate: (minutes: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val hours = remember { initHours }
    val minutes = remember { initMinutes }
    Box(modifier = modifier) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimePickerLazyColumn(
                timePickType = TimePickType.Hours,
                initValue = hours,
                onValueUpdate = onHoursUpdate,
                modifier = Modifier.weight(1f)
            )
            HoursMinutesDivider()
            TimePickerLazyColumn(
                timePickType = TimePickType.Minutes,
                initValue = minutes,
                onValueUpdate = onMinutesUpdate,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
private fun TimePickerLazyColumn(
    timePickType: TimePickType,
    initValue: Int,
    onValueUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.height(targetHeight)) {
        val seed = remember { timePickType.seed }
        val initIndex = remember { seed + initValue }
        val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initIndex)
        val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
        LazyColumn(
            state = lazyListState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            items(Int.MAX_VALUE) { item ->
                val value = remember {
                    item % timePickType.base
                }
                ItemTimeValue(value = value)
            }
        }
        BoxCurrentFrame(modifier = Modifier.align(Alignment.Center))
        LaunchedEffect(Unit) {
            snapshotFlow { lazyListState.firstVisibleItemIndex }
                .debounce(DEFAULT_DEBOUNCE_MILLIS)
                .distinctUntilChanged()
                .map { (it + 1) % timePickType.base }
                .collectLatest {
                    onValueUpdate(it)
                }
        }
    }
}

@Composable
private fun ItemTimeValue(
    value: Int
) {
    val text = remember { value.insertZeroIfRequired() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun BoxCurrentFrame(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight)
            .border(
                width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
    )
}

@Composable
private fun HoursMinutesDivider(modifier: Modifier = Modifier) {
    Text(
        text = ":",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

private fun Int.insertZeroIfRequired(): String = this.let { value ->
    if (this <= 9) "0$value" else "$value"
}

private const val HOURS_BASE = 24
private const val HOURS_SEED = 89_478_479

private const val MINUTES_BASE = 60
private const val MINUTES_SEED = 35_791_379

private enum class TimePickType(
    val base: Int,
    val seed: Int
) {
    Hours(
        base = HOURS_BASE,
        seed = HOURS_SEED
    ),
    Minutes(
        base = MINUTES_BASE,
        seed = MINUTES_SEED
    )
}