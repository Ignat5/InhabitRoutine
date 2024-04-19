package com.example.inhabitroutine.core.presentation.ui.dialog.pick_date

import com.example.inhabitroutine.core.presentation.base.BaseResultStateHolder
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenEvent
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenState
import com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.model.PickDateRequestModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    private val daysInMonthState = currentStartOfMonthDateState.map { startOfMonthDate ->
        calculateDaysInMonth(startOfMonthDate)
    }.stateIn(
        holderScope,
        SharingStarted.Eagerly,
        calculateDaysInMonth(currentStartOfMonthDateState.value)
    )

    private val availableDateRange = requestModel.minDate..requestModel.maxDate

    override val uiScreenState: StateFlow<PickDateScreenState> =
        combine(
            currentStartOfMonthDateState,
            currentPickedDateState,
            daysInMonthState
        ) { currentStartOfMonthDate, currentPickedDate, daysInMonth ->
            PickDateScreenState(
                startOfMonthDate = currentStartOfMonthDate,
                currentPickedDate = currentPickedDate,
                daysInMonth = daysInMonth,
                todayDate = todayDate,
                availableDateRange = availableDateRange
            )
        }.stateIn(
            holderScope,
            SharingStarted.Eagerly,
            PickDateScreenState(
                startOfMonthDate = currentStartOfMonthDateState.value,
                currentPickedDate = currentPickedDateState.value,
                daysInMonth = daysInMonthState.value,
                todayDate = todayDate,
                availableDateRange = availableDateRange
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