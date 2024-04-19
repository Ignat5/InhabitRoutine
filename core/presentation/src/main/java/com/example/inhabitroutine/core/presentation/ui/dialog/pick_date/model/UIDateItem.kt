package com.example.inhabitroutine.core.presentation.ui.dialog.pick_date.model

data class UIDateItem(
    val dayOfMonth: Int,
    val epochDay: Int,
    val status: Status
) {
    enum class Status { Current, Today, Day, Locked, OtherMonth }
}
