package com.example.inhabitroutine.core.presentation.ui.dialog.pick_date

import androidx.core.util.toRange
import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenEvent
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenState
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.model.PickDateRequestModel
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.model.UIDateItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class PickDateStateHolder(
    private val requestModel: PickDateRequestModel,
    private val defaultDispatcher: CoroutineDispatcher,
    override val holderScope: CoroutineScope
) : BaseResultStateHolder<PickDateScreenEvent, PickDateScreenState, PickDateScreenResult>() {
    private val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private val currentStartOfMonthDateState =
        MutableStateFlow(requestModel.initDate.firstDayOfMonth)
    private val currentPickedDateState = MutableStateFlow(requestModel.initDate)

    private val allDateItemsState = combine(
        currentStartOfMonthDateState.map { startOfMonth ->
            withContext(defaultDispatcher) {
                startOfMonth.provideDateItems(
                    minDate = requestModel.minDate,
                    maxDate = requestModel.maxDate
                )
            }
        },
        currentPickedDateState
    ) { allDateItems, currentPickedDate ->
        withContext(defaultDispatcher) {
            allDateItems.map { uiDateItem ->
                async {
                    when (uiDateItem.date) {
                        currentPickedDate -> UIDateItem.PickAble.Current(uiDateItem.date)
                        todayDate -> UIDateItem.PickAble.Today(uiDateItem.date)
                        else -> uiDateItem
                    }
                }
            }.awaitAll()
        }
    }
        .stateIn(
            holderScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    override val uiScreenState: StateFlow<PickDateScreenState> =
        combine(
            currentStartOfMonthDateState,
            currentPickedDateState,
            allDateItemsState
        ) { currentStartOfMonthDate, currentPickedDate, allDateItems ->
            PickDateScreenState(
                startOfMonthDate = currentStartOfMonthDate,
                currentPickedDate = currentPickedDate,
                allDateItems = allDateItems
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            PickDateScreenState(
                startOfMonthDate = currentStartOfMonthDateState.value,
                currentPickedDate = currentPickedDateState.value,
                allDateItems = allDateItemsState.value
            )
        )

    override fun onEvent(event: PickDateScreenEvent) {
        when (event) {
            is PickDateScreenEvent.OnDismissRequest ->
                onDismissRequest()

            else -> Unit
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickDateScreenResult.Dismiss)
    }

    private suspend fun LocalDate.provideDateItems(
        minDate: LocalDate,
        maxDate: LocalDate
    ) = this.firstDayOfMonth.let { startOfMonthDate ->
        val pickAbleDateRange = minDate..maxDate
        startOfMonthDate.provideCalendarRange().let { calendarRange ->
            calendarRange.start.daysUntil(calendarRange.endInclusive).let { daysCount ->
                coroutineScope {
                    (0..daysCount).map { offset ->
                        async {
                            calendarRange.start.plus(offset, DateTimeUnit.DAY).let { nextDate ->
                                when {
                                    nextDate.month != startOfMonthDate.month -> {
                                        UIDateItem.UnPickAble.OtherMonth(nextDate)
                                    }

                                    nextDate !in pickAbleDateRange -> {
                                        UIDateItem.UnPickAble.Locked(nextDate)
                                    }

                                    else -> {
                                        UIDateItem.PickAble.Day(nextDate)
                                    }
                                }
                            }
                        }
                    }.awaitAll()
                }
            }
        }
    }

    private fun LocalDate.provideCalendarRange() = this.firstDayOfMonth.let { startOfMonthDate ->
        startOfMonthDate.dayOfWeek.ordinal.let { diff ->
            startOfMonthDate.minus(diff, DateTimeUnit.DAY).let { startOfCalendarDate ->
                startOfMonthDate.plus(1, DateTimeUnit.MONTH).let { startOfNextMonthDate ->
                    startOfCalendarDate..startOfNextMonthDate.minus(1, DateTimeUnit.DAY)
                }
            }
        }
    }

    private val LocalDate.firstDayOfMonth
        get() = this.copy(dayOfMonth = FIRST_DAY_OF_MONTH)

    private fun LocalDate.copy(
        year: Int? = null,
        monthNumber: Int? = null,
        dayOfMonth: Int? = null
    ) = this.let { oldDate ->
        LocalDate(
            year = year ?: oldDate.year,
            monthNumber = monthNumber ?: oldDate.monthNumber,
            dayOfMonth = dayOfMonth ?: oldDate.dayOfMonth
        )
    }

    companion object {
        private const val FIRST_DAY_OF_MONTH = 1
    }

}