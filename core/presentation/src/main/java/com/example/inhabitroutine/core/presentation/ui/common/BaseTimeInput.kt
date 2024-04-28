package com.example.inhabitroutine.core.presentation.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

private val targetHeight get() = TextFieldDefaults.MinHeight
private const val DEFAULT_DEBOUNCE_MILLIS = 200L

@Composable
fun BaseTimeInput(
    initHours: Int,
    initMinutes: Int,
    onHoursUpdate: (hours: Int) -> Unit,
    onMinutesUpdate: (minutes: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val hours = remember { initHours }
    val minutes = remember { initMinutes }
    val scope = rememberCoroutineScope()
    val hoursLazyListState = rememberLazyListState(initialFirstVisibleItemIndex = hours)
    val minutesLazyListState = rememberLazyListState(initialFirstVisibleItemIndex = minutes)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(onClick = {
            val newLocalTime = LocalTime(
                hour = hoursLazyListState.firstVisibleItemIndex,
                minute = minutesLazyListState.firstVisibleItemIndex
            ).toSecondOfDay().let { currentSecondOfDay ->
                (currentSecondOfDay - (60 * 10)).let { newSecondOfDay ->
                    if (newSecondOfDay >= 0) {
                        LocalTime.fromSecondOfDay(newSecondOfDay)
                    } else LocalTime.fromSecondOfDay(0)
                }
            }
            scope.launch {
                launch {
                    minutesLazyListState.scrollToItem(newLocalTime.minute)
                }

                launch {
                    hoursLazyListState.scrollToItem(newLocalTime.hour)
                }
            }
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
        TimeInputLazyColumn(
            timeInputType = TimeInputType.Hours,
            lazyListState = hoursLazyListState,
//            initValue = hours,
            onValueUpdate = onHoursUpdate,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        HoursMinutesDivider()
        Spacer(modifier = Modifier.width(8.dp))
        TimeInputLazyColumn(
            timeInputType = TimeInputType.Minutes,
            lazyListState = minutesLazyListState,
//            initValue = minutes,
            onValueUpdate = onMinutesUpdate,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            val newLocalTime = LocalTime(
                hour = hoursLazyListState.firstVisibleItemIndex,
                minute = minutesLazyListState.firstVisibleItemIndex
            ).toSecondOfDay().let { currentSecondOfDay ->
                (currentSecondOfDay + (10 * 60)).let { newSecondOfDay ->
                    if (newSecondOfDay <= (23 * 3600) + (59 * 60)) {
                        LocalTime.fromSecondOfDay(newSecondOfDay)
                    } else LocalTime.fromSecondOfDay(0)
                }
            }
            scope.launch {
                minutesLazyListState.scrollToItem(newLocalTime.minute)
                hoursLazyListState.scrollToItem(newLocalTime.hour)
            }
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
private fun TimeInputLazyColumn(
    timeInputType: TimeInputType,
//    initValue: Int,
    lazyListState: LazyListState,
    onValueUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(targetHeight),
        contentAlignment = Alignment.Center
    ) {
//        val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = initValue)
        val flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
        val range = remember { timeInputType.range.toList() }
        LazyColumn(
            state = lazyListState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            items(range) { item ->
                ItemTimeValue(item)
            }
        }
        BoxCurrentFrame(modifier = Modifier.align(Alignment.Center))
        LaunchedEffect(Unit) {
            snapshotFlow { lazyListState.firstVisibleItemIndex }
                .debounce(DEFAULT_DEBOUNCE_MILLIS)
                .collectLatest { index ->
                    onValueUpdate(index)
                }
        }
    }
}

@Composable
private fun ItemTimeValue(value: Int) {
    val text = remember {
        value.insertZeroIfRequired()
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(targetHeight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun Int.insertZeroIfRequired(): String = this.let { value ->
    if (this <= 9) "0$value" else "$value"
}

@Composable
private fun BoxCurrentFrame(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(targetHeight)
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

private const val HOURS_BASE = 24
private const val MINUTES_BASE = 60

private enum class TimeInputType(val base: Int) {
    Hours(HOURS_BASE),
    Minutes(MINUTES_BASE);

    val range: IntRange get() = IntRange(0, base - 1)
}