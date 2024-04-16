package com.example.inhabitroutine.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AppNavDest(val route: String) {
    data object ViewScheduleDestination : AppNavDest(VIEW_SCHEDULE_ROUTE)
    data object CreateTaskDestination : AppNavDest("$CREATE_TASK_ROUTE/{${TASK_ID_KEY}}")

    companion object {
        const val TASK_ID_KEY = "TASK_ID_KEY"
        private const val VIEW_SCHEDULE_ROUTE = "VIEW_SCHEDULE_ROUTE"
        private const val CREATE_TASK_ROUTE = "CREATE_TASK_ROUTE"

        fun buildCreateTaskRoute(taskId: String) = "$CREATE_TASK_ROUTE/$taskId"

        val taskIdNavArg: NamedNavArgument
            get() = navArgument(TASK_ID_KEY) {
                this.type = NavType.StringType
            }
    }
}