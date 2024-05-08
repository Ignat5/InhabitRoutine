package com.ignatlegostaev.inhabitroutine.navigation.root

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.navigation.AppNavDest

sealed class RootDestination(
    val destination: AppNavDest,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int
) {

    sealed class Main(
        destination: AppNavDest,
        titleResId: Int,
        iconResId: Int
    ) : RootDestination(
        destination = destination,
        titleResId = titleResId,
        iconResId = iconResId
    ) {
        data object ViewSchedule : Main(
            destination = AppNavDest.ViewScheduleDestination,
            titleResId = R.string.drawer_view_schedule_title,
            iconResId = R.drawable.ic_today
        )

        data object ViewHabits : Main(
            destination = AppNavDest.ViewHabitsDestination,
            titleResId = R.string.drawer_view_habits_title,
            iconResId = R.drawable.ic_habit
        )

        data object ViewTasks : Main(
            destination = AppNavDest.ViewTasksDestination,
            titleResId = R.string.drawer_view_tasks_title,
            iconResId = R.drawable.ic_task
        )
    }

    sealed class Other(
        destination: AppNavDest,
        titleResId: Int,
        iconResId: Int
    ) : RootDestination(
        destination = destination,
        titleResId = titleResId,
        iconResId = iconResId
    ) {
        data object Settings : Other(
            destination = AppNavDest.ViewSettingsDestination,
            titleResId = R.string.drawer_settings_title,
            iconResId = R.drawable.ic_settings
        )
    }

    companion object {
        val allMainDestinations: List<RootDestination.Main>
            get() = listOf(
                Main.ViewSchedule,
                Main.ViewHabits,
                Main.ViewTasks
            )

        val allOtherDestinations: List<RootDestination.Other>
            get() = listOf(Other.Settings)
    }

}