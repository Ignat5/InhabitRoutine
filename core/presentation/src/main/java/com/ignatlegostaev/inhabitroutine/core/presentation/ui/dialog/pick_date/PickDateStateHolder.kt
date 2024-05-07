package com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date

import com.ignatlegostaev.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenState
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.model.PickDateRequestModel
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.model.UIDateItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
        currentStartOfMonthDateState,
        currentPickedDateState
    ) { currentStartOfMonthDate, currentPickedDate ->
        withContext(defaultDispatcher) {
            currentStartOfMonthDate.provideDateItems(
                currentPickedDate = currentPickedDate,
                todayDate = todayDate,
                maxDate = requestModel.maxDate,
                minDate = requestModel.minDate
            )
        }
    }.stateIn(
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
            is PickDateScreenEvent.OnDateClick ->
                onDateClick(event)

            is PickDateScreenEvent.OnNextMonthClick ->
                onNextMonthClick()

            is PickDateScreenEvent.OnPrevMonthClick ->
                onPrevMonthClick()

            is PickDateScreenEvent.OnConfirmClick ->
                onConfirmClick()

            is PickDateScreenEvent.OnDismissRequest ->
                onDismissRequest()
        }
    }

    private fun onDateClick(event: PickDateScreenEvent.OnDateClick) {
        currentPickedDateState.update { event.date }
    }

    private fun onNextMonthClick() {
        currentStartOfMonthDateState.update { oldDate ->
            oldDate.plus(1, DateTimeUnit.MONTH)
        }
    }

    private fun onPrevMonthClick() {
        currentStartOfMonthDateState.update { oldDate ->
            oldDate.minus(1, DateTimeUnit.MONTH)
        }
    }

    private fun onDismissRequest() {
        setUpResult(PickDateScreenResult.Dismiss)
    }

    private fun onConfirmClick() {
        setUpResult(
            PickDateScreenResult.Confirm(date = currentPickedDateState.value)
        )
    }

    private suspend fun LocalDate.provideDateItems(
        currentPickedDate: LocalDate,
        todayDate: LocalDate,
        maxDate: LocalDate,
        minDate: LocalDate
    ): List<UIDateItem> = this.firstDayOfMonth.let { startOfMonth ->
        startOfMonth.minus(startOfMonth.dayOfWeek.ordinal, DateTimeUnit.DAY)
            .let { startOfCalendar ->
                startOfMonth.plus(1, DateTimeUnit.MONTH).let { startOfNextMonth ->
                    startOfNextMonth.minus(1, DateTimeUnit.DAY).let { endOfCalendar ->
                        val currentMonthRange =
                            startOfMonth.toEpochDays() until startOfNextMonth.toEpochDays()
                        val availableDatesRange = minDate.toEpochDays()..maxDate.toEpochDays()
                        val currentDateEpochDay = currentPickedDate.toEpochDays()
                        val todayDateEpochDay = todayDate.toEpochDays()
                        coroutineScope {
                            (startOfCalendar.toEpochDays()..endOfCalendar.toEpochDays()).map { nextEpochDay ->
                                async {
                                    UIDateItem(
                                        dayOfMonth = (nextEpochDay - currentMonthRange.first) + 1,
                                        epochDay = nextEpochDay,
                                        status = when (nextEpochDay) {
                                            !in currentMonthRange -> UIDateItem.Status.OtherMonth
                                            !in availableDatesRange -> UIDateItem.Status.Locked
                                            currentDateEpochDay -> UIDateItem.Status.Current
                                            todayDateEpochDay -> UIDateItem.Status.Today
                                            else -> UIDateItem.Status.Day
                                        }
                                    )
                                }
                            }.awaitAll()
                        }
                    }
                }
            }
    }

    private fun calculateDaysInMonth(date: LocalDate) =
        date.firstDayOfMonth.let { startOfMonthDate ->
            startOfMonthDate.plus(1, DateTimeUnit.MONTH).let { startOfNextMonth ->
                startOfMonthDate.daysUntil(startOfNextMonth)
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