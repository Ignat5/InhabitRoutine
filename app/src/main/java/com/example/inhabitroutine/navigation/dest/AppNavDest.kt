package com.example.inhabitroutine.navigation.dest

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AppNavDest(val route: String) {
    data object ViewScheduleDestination : AppNavDest(VIEW_SCHEDULE_ROUTE)

    class NavArgs {
        companion object {
            const val TASK_ID_KEY = "TASK_ID_KEY"
            val taskIdNavArg
                get() = navArgument(TASK_ID_KEY) {
                    this.type = NavType.StringType
                }
        }
    }

    companion object {
        private const val VIEW_SCHEDULE_ROUTE = "VIEW_SCHEDULE_ROUTE"
    }
}