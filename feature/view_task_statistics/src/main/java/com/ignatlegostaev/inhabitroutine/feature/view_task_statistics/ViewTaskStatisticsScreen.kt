package com.ignatlegostaev.inhabitroutine.feature.view_task_statistics

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ignatlegostaev.inhabitroutine.core.presentation.R
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskStatus
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskCompletionCount
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.calculate_statistics.TaskStreakModel
import com.ignatlegostaev.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_task_statistics.components.ViewTaskStatisticsScreenState

private const val PROGRESS_INDICATOR_SIZE_DP = 100
private const val PROGRESS_INDICATOR_STROKE_WIDTH_DP = 2
private const val SPACE_BETWEEN_SECTIONS_DP = 32

@Composable
fun ViewTaskStatisticsScreen(
    state: ViewTaskStatisticsScreenState,
    onEvent: (ViewTaskStatisticsScreenEvent) -> Unit
) {
    Scaffold(
        topBar = {
            ScreenTopBar(
                taskTitle = state.taskModel?.title,
                onBack = {
                    onEvent(ViewTaskStatisticsScreenEvent.OnLeaveRequest)
                }
            )
        },
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        BackHandler { onEvent(ViewTaskStatisticsScreenEvent.OnLeaveRequest) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(SPACE_BETWEEN_SECTIONS_DP.dp)
            ) {
                HabitScoreSection(habitScore = state.taskStatisticsModel.habitScore)
                StreakSection(streakModel = state.taskStatisticsModel.streakModel)
                CompletionSection(completionCount = state.taskStatisticsModel.completionCount)
                StatusCountSection(statusCount = state.taskStatisticsModel.statusCount)
            }
        }
    }
}

@Composable
private fun HabitScoreSection(habitScore: Float) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(titleResId = R.string.task_statistics_habit_score_title)
        Spacer(modifier = Modifier.height(8.dp))
        val progressState by animateFloatAsState(
            targetValue = habitScore,
            animationSpec = spring(stiffness = Spring.StiffnessVeryLow)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progressState },
                modifier = Modifier.size(PROGRESS_INDICATOR_SIZE_DP.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                strokeWidth = PROGRESS_INDICATOR_STROKE_WIDTH_DP.dp
            )
            val habitScorePercent = remember(habitScore) {
                (habitScore * 100).toInt()
            }
            Text(
                text = "$habitScorePercent",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private enum class StreakType {
    Current,
    Best
}

@Composable
private fun StreakSection(
    streakModel: TaskStreakModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(titleResId = R.string.task_statistics_streak_title)
        Spacer(modifier = Modifier.height(8.dp))
        val allStreakTypes = remember { StreakType.entries }
        allStreakTypes.forEachIndexed { index, item ->
            val titleResId = remember {
                when (item) {
                    StreakType.Current -> R.string.task_statistics_current_streak_title
                    StreakType.Best -> R.string.task_statistics_best_streak_title
                }
            }
            val data = remember(streakModel) {
                when (item) {
                    StreakType.Current -> streakModel.currentStreak
                    StreakType.Best -> streakModel.bestStreak
                }
            }
            ItemStatistics(titleResId = titleResId, data = "$data")
            if (index != allStreakTypes.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

private enum class CompletionType {
    Week,
    Month,
    Year,
    All
}

@Composable
private fun CompletionSection(
    completionCount: TaskCompletionCount
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(titleResId = R.string.task_statistics_times_completed_title)
        Spacer(modifier = Modifier.height(8.dp))
        val allCompletionTypes = remember { CompletionType.entries }
        allCompletionTypes.forEachIndexed { index, item ->
            val titleResId = remember {
                when (item) {
                    CompletionType.Week -> R.string.task_statistics_completion_count_week_title
                    CompletionType.Month -> R.string.task_statistics_completion_count_month_title
                    CompletionType.Year -> R.string.task_statistics_completion_count_year_title
                    CompletionType.All -> R.string.task_statistics_completion_count_all_time_title
                }
            }
            val data = remember(completionCount) {
                when (item) {
                    CompletionType.Week -> completionCount.currentWeekCompletionCount
                    CompletionType.Month -> completionCount.currentMonthCompletionCount
                    CompletionType.Year -> completionCount.currentYearCompletionCount
                    CompletionType.All -> completionCount.currentYearCompletionCount
                }
            }
            ItemStatistics(titleResId = titleResId, data = "$data")
            if (index != allCompletionTypes.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

private enum class StatusType {
    Completed,
    Pending,
    Skipped,
    Failed
}

@Composable
private fun StatusCountSection(statusCount: Map<TaskStatus, Int>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(titleResId = R.string.task_statistics_status_count_title)
        Spacer(modifier = Modifier.height(8.dp))
        val allStatusTypes = remember { StatusType.entries }
        allStatusTypes.forEachIndexed { index, item ->
            val titleResId = remember {
                when (item) {
                    StatusType.Completed -> R.string.task_status_done
                    StatusType.Pending -> R.string.task_status_pending
                    StatusType.Skipped -> R.string.task_status_skipped
                    StatusType.Failed -> R.string.task_status_failed
                }
            }
            val data = remember(statusCount) {
                when (item) {
                    StatusType.Completed -> statusCount[TaskStatus.Completed] ?: 0
                    StatusType.Pending -> statusCount[TaskStatus.NotCompleted.Pending] ?: 0
                    StatusType.Skipped -> statusCount[TaskStatus.NotCompleted.Skipped] ?: 0
                    StatusType.Failed -> statusCount[TaskStatus.NotCompleted.Failed] ?: 0
                }
            }
            ItemStatistics(titleResId = titleResId, data = "$data")
            if (index != allStatusTypes.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun ItemStatistics(
    @StringRes titleResId: Int,
    data: String
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(id = titleResId),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = data,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun SectionTitle(
    @StringRes titleResId: Int
) {
    Text(
        text = stringResource(id = titleResId),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenTopBar(
    taskTitle: String?,
    onBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = taskTitle ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }
        }
    )
}