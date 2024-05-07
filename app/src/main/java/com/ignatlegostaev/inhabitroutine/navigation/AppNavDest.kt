package com.ignatlegostaev.inhabitroutine.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AppNavDest(val route: String) {
    data object RootGraphDestination : AppNavDest(ROOT_GRAPH_ROUTE)
    data object ViewScheduleDestination : AppNavDest(VIEW_SCHEDULE_ROUTE)
    data object CreateTaskDestination : AppNavDest("$CREATE_TASK_ROUTE/{${TASK_ID_KEY}}")
    data object ViewRemindersDestination : AppNavDest("$VIEW_REMINDERS_ROUTE/{$TASK_ID_KEY}")
    data object SearchTasksDestination : AppNavDest(SEARCH_TASKS_ROUTE)
    data object EditTaskDestination : AppNavDest("$EDIT_TASK_ROUTE/{$TASK_ID_KEY}")
    data object ViewTaskStatisticsDestination : AppNavDest("$VIEW_TASK_STATISTICS_ROUTE/{$TASK_ID_KEY}")
    data object ViewTasksDestination : AppNavDest(VIEW_TASKS_ROUTE)
    data object ViewHabitsDestination : AppNavDest(VIEW_HABITS_ROUTE)
    data object ViewSettingsDestination : AppNavDest(VIEW_SETTINGS_ROUTE)

    companion object {
        const val TASK_ID_KEY = "TASK_ID_KEY"
        private const val ROOT_GRAPH_ROUTE = "ROOT_GRAPH_ROUTE"
        private const val VIEW_SCHEDULE_ROUTE = "VIEW_SCHEDULE_ROUTE"
        private const val CREATE_TASK_ROUTE = "CREATE_TASK_ROUTE"
        private const val VIEW_REMINDERS_ROUTE = "VIEW_REMINDERS_ROUTE"
        private const val SEARCH_TASKS_ROUTE = "SEARCH_TASKS_ROUTE"
        private const val EDIT_TASK_ROUTE = "EDIT_TASK_ROUTE"
        private const val VIEW_TASK_STATISTICS_ROUTE = "VIEW_TASK_STATISTICS_ROUTE"
        private const val VIEW_TASKS_ROUTE = "VIEW_TASKS_ROUTE"
        private const val VIEW_HABITS_ROUTE = "VIEW_HABITS_ROUTE"
        private const val VIEW_SETTINGS_ROUTE = "VIEW_SETTINGS_ROUTE"

        fun buildCreateTaskRoute(taskId: String) = "$CREATE_TASK_ROUTE/$taskId"
        fun buildViewRemindersRoute(taskId: String) = "$VIEW_REMINDERS_ROUTE/$taskId"
        fun buildEditTaskRoute(taskId: String) = "$EDIT_TASK_ROUTE/$taskId"
        fun buildViewTaskStatisticsRoute(taskId: String) = "$VIEW_TASK_STATISTICS_ROUTE/$taskId"

        val taskIdNavArg: NamedNavArgument
            get() = navArgument(TASK_ID_KEY) {
                this.type = NavType.StringType
            }
    }
}