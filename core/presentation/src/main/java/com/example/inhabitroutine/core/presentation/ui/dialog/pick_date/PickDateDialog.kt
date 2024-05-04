package com.example.inhabitroutine.core.presentation.ui.dialog.pick_date

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.inhabitroutine.core.presentation.R
import com.example.inhabitroutine.core.presentation.ui.base.BaseDialogWithResult
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogActionType
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseDialogDefaults
import com.example.inhabitroutine.core.presentation.ui.dialog.base.BaseStaticDialog
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenEvent
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenState
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.model.UIDateItem
import com.example.inhabitroutine.core.presentation.ui.util.toDayOfWeekMonthMonthDayDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toMonthDayYearDisplay
import com.example.inhabitroutine.core.presentation.ui.util.toMonthYearDisplay
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus

@Composable
fun PickDateDialog(
    stateHolder: PickDateStateHolder,
    onResult: (PickDateScreenResult) -> Unit
) {
    BaseDialogWithResult(stateHolder = stateHolder, onResult = onResult) { state, onEvent ->
        if (state.allDateItems.isNotEmpty()) {
            PickDateDialogStateless(state, onEvent)
        }
    }
}

@Composable
private fun PickDateDialogStateless(
    state: PickDateScreenState,
    onEvent: (PickDateScreenEvent) -> Unit
) {
    val context = LocalContext.current
    BaseDialog(
        onDismissRequest = { onEvent(PickDateScreenEvent.OnDismissRequest) },
        dialogPadding = PaddingValues(horizontal = 12.dp, vertical = 24.dp),
        title = {
            BaseDialogDefaults.BaseDialogTitle(
                titleText = state.currentPickedDate.toDayOfWeekMonthMonthDayDisplay(context),
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        },
        actionType = BaseDialogActionType.ConfirmDismiss(
            confirmButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = { onEvent(PickDateScreenEvent.OnConfirmClick) }
                )
            },
            dismissButton = {
                BaseDialogDefaults.BaseActionButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = { onEvent(PickDateScreenEvent.OnDismissRequest) }
                )
            }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MonthController(
                currentDate = state.startOfMonthDate,
                onPrevClick = { onEvent(PickDateScreenEvent.OnPrevMonthClick) },
                onNextClick = { onEvent(PickDateScreenEvent.OnNextMonthClick) }
            )
            MonthGrid(
                allDateItems = state.allDateItems,
                onDateClick = { onEvent(PickDateScreenEvent.OnDateClick(it)) }
            )
        }
    }
}

@Composable
private fun MonthController(
    currentDate: LocalDate,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_previous),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
        Text(
            text = remember(currentDate) {
                currentDate.toMonthYearDisplay(context)
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onNextClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_next),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun MonthGrid(
    allDateItems: List<UIDateItem>,
    onDateClick: (LocalDate) -> Unit
) {
    val allDaysOfWeek = remember { DayOfWeek.entries }
    LazyVerticalGrid(
        columns = GridCells.Fixed(allDaysOfWeek.size),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = allDaysOfWeek,
            contentType = { ItemContentType.DayOfWeekLabel }
        ) { item ->
            ItemDayOfWeek(item)
        }
        items(
            items = allDateItems,
            key = { item -> item.dayOfMonth },
            contentType = { ItemContentType.DayOfMonth }
        ) { item ->
            ItemDayOfMonth(
                item = item,
                onClick = {
                    onDateClick(LocalDate.fromEpochDays(item.epochDay))
                }
            )
        }
    }
}

@Composable
private fun ItemDayOfMonth(
    item: UIDateItem,
    onClick: () -> Unit
) {
    if (item.status != UIDateItem.Status.OtherMonth) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.extraLarge)
                .then(
                    when (item.status) {
                        UIDateItem.Status.Current -> {
                            Modifier.background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                        }

                        UIDateItem.Status.Today -> {
                            Modifier.border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                        }

                        else -> Modifier
                    }
                )
                .clickable(
                    enabled = item.status != UIDateItem.Status.Locked,
                    onClick = onClick
                )
        ) {
            val label = remember(item) {
                "${item.dayOfMonth}"
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = when (item.status) {
                    UIDateItem.Status.Current -> MaterialTheme.colorScheme.onPrimary
                    UIDateItem.Status.Today -> MaterialTheme.colorScheme.primary
                    UIDateItem.Status.Locked -> MaterialTheme.colorScheme.outline
                    else -> MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
private fun ItemDayOfWeek(
    dayOfWeek: DayOfWeek,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val label = remember { dayOfWeek.toDisplay(context).take(1) }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

private enum class ItemContentType {
    DayOfWeekLabel,
    DayOfMonth
}