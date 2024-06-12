package com.ignatlegostaev.inhabitroutine.feature.view_schedule

import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.presentation.components.config.BaseScreenConfig
import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.BaseScreenNavigation
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_progress_type.PickTaskProgressTypeScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_task_type.PickTaskTypeScreenResult
import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitNumberFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitTimeFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.RecurringTaskFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskExtras
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskStatus
import com.ignatlegostaev.inhabitroutine.domain.model.derived.TaskWithExtrasAndRecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.content.RecordEntry
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskProgressType
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.TaskType
import com.ignatlegostaev.inhabitroutine.domain.record.api.DeleteRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.record.api.SaveRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksWithExtrasAndRecordByDateUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.components.ViewScheduleScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_number_record.components.EnterTaskNumberRecordScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.enter_time_record.components.EnterTaskTimeRecordScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.config.view_task_actions.components.ViewTaskActionsScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.util.DefaultSortTasksUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_schedule.util.SortTasksUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyBlocking
import org.mockito.kotlin.wheneverBlocking

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ViewScheduleViewModelTest {

    private lateinit var viewModel: ViewScheduleViewModel
    private lateinit var fakeReadTasksWithExtrasAndRecordByDateUseCase: FakeReadTasksWithExtrasAndRecordByDateUseCase
    private lateinit var spySortTasksUseCase: SortTasksUseCase
    private lateinit var testDispatcher: TestDispatcher

    @Mock
    private lateinit var saveTaskDraftUseCase: SaveTaskDraftUseCase

    @Mock
    private lateinit var saveRecordUseCase: SaveRecordUseCase

    @Mock
    private lateinit var deleteRecordUseCase: DeleteRecordUseCase

    @Before
    fun setUp() {
        initDependencies()
        initViewModel()
    }

    @Test
    fun `when read task use case emits tasks, then sorting is applied`() = runLocalTest {
        collectUIState()
        val task1 = buildTestSingleTaskWithExtrasAndRecord()
        val allTasks = listOf(task1)
        fillReadTasksUseCaseWithTasks(allTasks)
        advanceUntilIdle()
        verify(spySortTasksUseCase).invoke(allTasks)
    }

    @Test
    fun `when read tasks use case emits new tasks, then state is updated correctly`() =
        runLocalTest {
            collectUIState()
            fillReadTasksUseCaseWithTasks(emptyList())
            advanceUntilIdle()
            getScreenStateTasksWithExtrasAndRecord().apply {
                assertThat(this).isEmpty()
            }
            val task1 = buildSingleTaskWithExtrasAndRecordInCompletedState()
            fillReadTasksUseCaseWithTasks(listOf(task1))
            advanceUntilIdle()
            getScreenStateTasksWithExtrasAndRecord().apply {
                assertThat(this).containsExactly(task1)
            }
        }

    private fun getScreenStateTasksWithExtrasAndRecord(): List<TaskWithExtrasAndRecordModel> {
        return (viewModel.uiScreenState.value.allTasksResult.data) ?: emptyList()
    }

    @Test
    fun `when 'on search click' event is sent, then navigation is set to 'search tasks'`() =
        runLocalTest {
            sendOnSearchClickEvent()
            advanceUntilIdle()
            verifyNavigationStateIsSetToSearchTasks()
        }

    private fun verifyNavigationStateIsSetToSearchTasks() {
        verifyNavigationState(ViewScheduleScreenNavigation.SearchTasks)
    }

    private fun sendOnSearchClickEvent() {
        viewModel.onEvent(ViewScheduleScreenEvent.OnSearchClick)
    }

    @Test
    fun `when 'pick date click' event is sent, the config is set to 'pick date'`() = runLocalTest {
        sendPickDateClickEvent()
        advanceUntilIdle()
        verifyConfigIsSetToPickDate()
    }

    private fun verifyConfigIsSetToPickDate() {
        verifyConfigIsInstanceOf(ViewScheduleScreenConfig.PickDate::class.java)
    }

    private fun sendPickDateClickEvent() {
        viewModel.onEvent(ViewScheduleScreenEvent.OnPickDateClick)
    }

    @Test
    fun `when 'onPrevWeekClick' event is sent, then start of week state is set correctly`() =
        runLocalTest {
            collectUIState()
            advanceUntilIdle()
            val currentStartOfWeekDate = getCurrentStartOfWeekDate()
            sendOnPrevWeekClickEvent()
            advanceUntilIdle()
            val newStartOfWeekDate = getCurrentStartOfWeekDate()
            val expectedDate = currentStartOfWeekDate.minus(1, DateTimeUnit.WEEK)
            assertThat(newStartOfWeekDate).isEqualTo(expectedDate)
        }

    private fun sendOnPrevWeekClickEvent() {
        viewModel.onEvent(ViewScheduleScreenEvent.OnPrevWeekClick)
    }

    @Test
    fun `when 'onNextWeekClick' event is sent, then start of week state is set correctly`() =
        runLocalTest {
            collectUIState()
            advanceUntilIdle()
            val currentStartOfWeekDate = getCurrentStartOfWeekDate()
            sendOnNextWeekClickEvent()
            advanceUntilIdle()
            val newStartOfWeekDate = getCurrentStartOfWeekDate()
            val expectedDate = currentStartOfWeekDate.plus(1, DateTimeUnit.WEEK)
            assertThat(newStartOfWeekDate).isEqualTo(expectedDate)
        }

    private fun getCurrentStartOfWeekDate(): LocalDate {
        return viewModel.uiScreenState.value.startOfWeekDate
    }

    private fun sendOnNextWeekClickEvent() {
        viewModel.onEvent(ViewScheduleScreenEvent.OnNextWeekClick)
    }

    @Test
    fun `when 'on date click' event is sent, then current date is set accordingly`() =
        runLocalTest {
            collectUIState()
            advanceUntilIdle()
            val beforeDate = viewModel.uiScreenState.value.currentDate
            val afterDate = beforeDate.plus(1, DateTimeUnit.DAY)
            sendOnDateClickEvent(afterDate)
            advanceUntilIdle()
            verifyCurrentDateIsSetTo(afterDate)
        }

    private fun sendOnDateClickEvent(date: LocalDate) {
        viewModel.onEvent(ViewScheduleScreenEvent.OnDateClick(date))
    }

    private fun verifyCurrentDateIsSetTo(date: LocalDate) {
        assertThat(viewModel.uiScreenState.value.currentDate).isEqualTo(date)
    }

    @Test
    fun `when 'task long click' event is sent, then config state is set to 'view task actions'`() =
        runLocalTest {
            collectUIState()
            val targetTask = buildSingleTaskWithExtrasAndRecordInCompletedState()
            fillReadTasksUseCaseWithTasks(listOf(targetTask))
            advanceUntilIdle()
            sendOnTaskLongClickEventForTask(targetTask.task.id)
            advanceUntilIdle()
            verifyConfigStateIsSetToViewTaskActions()
        }

    private fun sendOnTaskLongClickEventForTask(taskId: String) {
        viewModel.onEvent(ViewScheduleScreenEvent.OnTaskLongClick(taskId))
    }

    private fun verifyConfigStateIsSetToViewTaskActions() {
        verifyConfigStateIsInstanceOf(ViewScheduleScreenConfig.ViewTaskActions::class.java)
    }

    @Test
    fun `when action-result 'enter progress' is sent for HabitTime, then config state is set to 'enter time record'`() =
        runLocalTest {
            collectUIState()
            advanceUntilIdle()
            val targetTask = buildTestHabitTimeWithExtrasAndRecord()
            val targetDate = viewModel.uiScreenState.value.currentDate
            fillReadTasksUseCaseWithTasks(listOf(targetTask))
            advanceUntilIdle()
            sendEnterProgressActionEventForTask(
                taskId = targetTask.task.id,
                date = targetDate
            )
            advanceUntilIdle()
            verifyConfigStateIsSetToEnterTimeRecord()
        }

    private fun verifyConfigStateIsSetToEnterTimeRecord() {
        verifyConfigIsInstanceOf(ViewScheduleScreenConfig.EnterTaskTimeRecord::class.java)
    }

    @Test
    fun `when action-result 'enter progress' is sent for HabitNumber, then config state is set to 'enter number record'`() =
        runLocalTest {
            collectUIState()
            advanceUntilIdle()
            val targetTask = buildTestHabitNumberWithExtrasAndRecord()
            val targetDate = viewModel.uiScreenState.value.currentDate
            fillReadTasksUseCaseWithTasks(listOf(targetTask))
            advanceUntilIdle()
            sendEnterProgressActionEventForTask(
                taskId = targetTask.task.id,
                date = targetDate
            )
            advanceUntilIdle()
            verifyConfigStateIsSetToEnterNumberRecord()
        }

    private fun verifyConfigStateIsSetToEnterNumberRecord() {
        verifyConfigIsInstanceOf(ViewScheduleScreenConfig.EnterTaskNumberRecord::class.java)
    }

    private fun sendEnterProgressActionEventForTask(
        taskId: String,
        date: LocalDate
    ) {
        viewModel.onEvent(
            ViewScheduleScreenEvent.ResultEvent.ViewTaskActions(
                ViewTaskActionsScreenResult.OnActionClick.EnterProgress(
                    taskId = taskId,
                    date = date
                )
            )
        )
    }

    @Test
    fun `when result 'pick progress type' is sent, then correct habit is created`() = runLocalTest {
        val pickedProgressType = TaskProgressType.entries.random()
        mockSaveTaskDraftForHabitWithProgressType(pickedProgressType)
        sendOnPickTaskProgressTypeEventWithProgressType(pickedProgressType)
        advanceUntilIdle()
        verifySaveTaskDraftCalledForHabitWithProgressType(pickedProgressType)
    }

    private fun mockSaveTaskDraftForHabitWithProgressType(progressType: TaskProgressType) {
        mockSaveTaskDraftUseCaseForRequestType(
            SaveTaskDraftUseCase.RequestType.CreateHabit(
                progressType
            )
        )
    }

    private fun sendOnPickTaskProgressTypeEventWithProgressType(progressType: TaskProgressType) {
        viewModel.onEvent(
            ViewScheduleScreenEvent.ResultEvent.PickTaskProgressType(
                PickTaskProgressTypeScreenResult.Confirm(progressType)
            )
        )
    }

    private fun verifySaveTaskDraftCalledForHabitWithProgressType(progressType: TaskProgressType) {
        verifyBlocking(saveTaskDraftUseCase) {
            invoke(SaveTaskDraftUseCase.RequestType.CreateHabit(progressType))
        }
    }

    @Test
    fun `when result 'pick task type' is sent with Habit, then config state is set to 'pick task type'`() =
        runLocalTest {
            sendConfirmPickTaskTypeEventWithType(TaskType.Habit)
            verifyConfigStateIsSetToPickTaskProgressType()
        }

    private fun verifyConfigStateIsSetToPickTaskProgressType() {
        verifyConfigStateIsInstanceOf(ViewScheduleScreenConfig.PickTaskProgressType::class.java)
    }

    @Test
    fun `when result 'pick task type' is sent with RecurringTask, then correct task is created`() =
        runLocalTest {
            mockSaveTaskDraftForRecurringTask()
            sendConfirmPickTaskTypeEventWithType(TaskType.RecurringTask)
            advanceUntilIdle()
            verifySaveTaskDraftUseCaseCalledOnceForRecurringTask()
        }

    private fun mockSaveTaskDraftForRecurringTask() {
        mockSaveTaskDraftUseCaseForRequestType(SaveTaskDraftUseCase.RequestType.CreateRecurringTask)
    }

    private fun verifySaveTaskDraftUseCaseCalledOnceForRecurringTask() {
        verifySaveTaskDraftUseCaseCalledOnceWithRequestType(SaveTaskDraftUseCase.RequestType.CreateRecurringTask)
    }

    @Test
    fun `when result 'pick task type' is sent with SingleTask, then correct task is created`() =
        runLocalTest {
            mockSaveTaskDraftForSingleTask()
            sendConfirmPickTaskTypeEventWithType(TaskType.SingleTask)
            advanceUntilIdle()
            verifySaveTaskDraftUseCaseCalledOnceForSingleTask()
        }

    private fun mockSaveTaskDraftForSingleTask(): ResultModel<String, Throwable> {
        return mockSaveTaskDraftUseCaseForRequestType(SaveTaskDraftUseCase.RequestType.CreateSingleTask)
    }

    private fun mockSaveTaskDraftUseCaseForRequestType(requestType: SaveTaskDraftUseCase.RequestType): ResultModel<String, Throwable> {
        ResultModel.success(randomUUID()).let { result ->
            wheneverBlocking {
                saveTaskDraftUseCase.invoke(requestType)
            }.thenReturn(result)
            return result
        }
    }

    private fun sendConfirmPickTaskTypeEventWithType(taskType: TaskType) {
        viewModel.onEvent(
            ViewScheduleScreenEvent.ResultEvent.PickTaskType(
                PickTaskTypeScreenResult.Confirm(taskType)
            )
        )
    }

    private fun verifySaveTaskDraftUseCaseCalledOnceForSingleTask() {
        verifySaveTaskDraftUseCaseCalledOnceWithRequestType(SaveTaskDraftUseCase.RequestType.CreateSingleTask)
    }

    private fun verifySaveTaskDraftUseCaseCalledOnceWithRequestType(requestType: SaveTaskDraftUseCase.RequestType) {
        verifyBlocking(saveTaskDraftUseCase) {
            invoke(requestType)
        }
    }

    @Test
    fun `when action 'create task click' is sent, then config state is set to 'pick task type'`() =
        runLocalTest {
            sendOnCreateTaskClickEvent()
            verifyConfigStateIsSetToPickTaskType()
        }

    private fun sendOnCreateTaskClickEvent() {
        viewModel.onEvent(ViewScheduleScreenEvent.OnCreateTaskClick)
    }

    private fun verifyConfigStateIsSetToPickTaskType() {
        verifyConfigStateIsInstanceOf(ViewScheduleScreenConfig.PickTaskType::class.java)
    }

    private fun verifyConfigStateEqualsTo(expected: ViewScheduleScreenConfig) {
        (viewModel.uiScreenConfigState.value as? BaseScreenConfig.Config)?.config?.let { config ->
            assertThat(config).isEqualTo(expected)
        }
    }

    private fun <T : ViewScheduleScreenConfig> verifyConfigStateIsInstanceOf(instanceOf: Class<T>) {
        (viewModel.uiScreenConfigState.value as? BaseScreenConfig.Config)?.config?.let { config ->
            assertThat(config).isInstanceOf(instanceOf)
        }
    }

    @Test
    fun `when action 'edit task' is sent, then the navigation state is set to 'edit task'`() =
        runLocalTest {
            collectUIState()
            val targetTask = buildTestSingleTaskWithExtrasAndRecord()
            fillReadTasksUseCaseWithTasks(listOf(targetTask))
            advanceUntilIdle()
            sendOnTaskEditResultEventForTask(targetTask.task.id)
            verifyNavigationStateIsSetToEditTaskForTask(targetTask.task.id)
        }

    private fun sendOnTaskEditResultEventForTask(taskId: String) {
        viewModel.onEvent(
            ViewScheduleScreenEvent.ResultEvent.ViewTaskActions(
                ViewTaskActionsScreenResult.OnEditClick(taskId)
            )
        )
    }

    private fun verifyNavigationStateIsSetToEditTaskForTask(taskId: String) {
        verifyNavigationState(ViewScheduleScreenNavigation.EditTask(taskId))
    }

    private fun verifyNavigationState(expected: ViewScheduleScreenNavigation) {
        (viewModel.uiScreenNavigationState.value as? BaseScreenNavigation.Destination)?.destination?.let { destination ->
            assertThat(destination).isEqualTo(expected)
        } ?: throw AssertionError()
    }

    @Test
    fun `when click event is sent for HabitTime, then 'enter time record' state is set`() =
        runLocalTest {
            collectUIState()
            val targetTask = buildTestHabitTimeWithExtrasAndRecord()
            fillReadTasksUseCaseWithTasks(listOf(targetTask))
            advanceUntilIdle()
            sendOnTaskClickEventForTask(targetTask.task.id)
            advanceUntilIdle()
            verifyConfigIsSetToEnterTaskTimeRecord()
        }

    private fun verifyConfigIsSetToEnterTaskTimeRecord() {
        verifyConfigIsInstanceOf(ViewScheduleScreenConfig.EnterTaskTimeRecord::class.java)
    }

    @Test
    fun `when click event is sent for HabitNumber, then 'enter number record' state is set`() =
        runLocalTest {
            collectUIState()
            val targetTask = buildTestHabitNumberWithExtrasAndRecord()
            fillReadTasksUseCaseWithTasks(listOf(targetTask))
            advanceUntilIdle()
            sendOnTaskClickEventForTask(targetTask.task.id)
            advanceUntilIdle()
            verifyConfigIsSetToEnterTaskNumberRecord()
        }

    private fun verifyConfigIsSetToEnterTaskNumberRecord() {
        verifyConfigIsInstanceOf(ViewScheduleScreenConfig.EnterTaskNumberRecord::class.java)
    }

    private fun <T : ViewScheduleScreenConfig> verifyConfigIsInstanceOf(expected: Class<T>) {
        val currentBaseConfig = viewModel.uiScreenConfigState.value
        if (currentBaseConfig is BaseScreenConfig.Config) {
            assertThat(currentBaseConfig.config).isInstanceOf(expected)
        } else throw AssertionError()
    }

    @Test
    fun `when click event is sent for RecurringTask in 'completed' state, then delete record use case is invoked`() {
        val targetTask = buildRecurringTaskWithExtrasAndRecordInCompletedState()
        val targetDate = viewModel.uiScreenState.value.currentDate
        runOnTaskClickEventTest(
            targetTaskWithExtrasAndRecord = targetTask,
            doMock = {
                mockDeleteRecordForRecurringTaskInCompletedState(
                    taskId = targetTask.task.id,
                    date = targetDate
                )
            },
            doVerify = {
                verifyDeleteRecordForRecurringTaskInCompletedState(
                    taskId = targetTask.task.id,
                    date = targetDate
                )
            }
        )
    }

    private fun buildRecurringTaskWithExtrasAndRecordInCompletedState(): TaskWithExtrasAndRecordModel.Task.RecurringTask {
        return buildTestRecurringTaskWithExtrasAndRecord(
            recordEntry = RecordEntry.Done,
            taskStatus = TaskStatus.Completed
        )
    }

    private fun mockDeleteRecordForRecurringTaskInCompletedState(taskId: String, date: LocalDate) {
        wheneverBlocking {
            deleteRecordUseCase.invoke(taskId = taskId, date = date)
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun verifyDeleteRecordForRecurringTaskInCompletedState(
        taskId: String,
        date: LocalDate
    ) {
        verifyBlocking(deleteRecordUseCase) {
            invoke(
                taskId = taskId,
                date = date
            )
        }
    }

    @Test
    fun `when click event is sent for RecurringTask in 'pending' state, then save record use case with 'done' is invoked`() {
        val targetTask = buildRecurringTaskWithExtrasAndRecordInPendingState()
        val targetDate = viewModel.uiScreenState.value.currentDate
        runOnTaskClickEventTest(
            targetTaskWithExtrasAndRecord = targetTask,
            doMock = {
                mockSaveTaskRecordForRecurringTaskInPendingState(
                    taskId = targetTask.task.id,
                    date = targetDate
                )
            },
            doVerify = {
                verifySaveTaskRecordUseCaseForRecurringTaskCalledOnce(
                    taskId = targetTask.task.id,
                    date = targetDate
                )
            }
        )
    }

    private fun buildRecurringTaskWithExtrasAndRecordInPendingState(): TaskWithExtrasAndRecordModel.Task.RecurringTask {
        return buildTestRecurringTaskWithExtrasAndRecord(
            taskStatus = TaskStatus.NotCompleted.Pending
        )
    }

    private fun mockSaveTaskRecordForRecurringTaskInPendingState(
        taskId: String,
        date: LocalDate
    ) {
        wheneverBlocking {
            saveRecordUseCase.invoke(
                taskId = taskId,
                date = date,
                requestType = SaveRecordUseCase.RequestType.EntryDone
            )
        }
    }

    private fun verifySaveTaskRecordUseCaseForRecurringTaskCalledOnce(
        taskId: String,
        date: LocalDate
    ) {
        verifyBlocking(saveRecordUseCase) {
            invoke(
                taskId = taskId,
                date = date,
                requestType = SaveRecordUseCase.RequestType.EntryDone
            )
        }
    }

    private fun buildTestRecurringTaskWithExtrasAndRecord(
        recordEntry: RecordEntry.TaskEntry? = null,
        taskStatus: TaskStatus.Task? = null,
        extras: TaskExtras.Task.RecurringTask? = null
    ): TaskWithExtrasAndRecordModel.Task.RecurringTask {
        return TaskWithExtrasAndRecordModel.Task.RecurringTask(
            task = RecurringTaskFactory().build(),
            taskExtras = extras ?: TaskExtras.Task.RecurringTask(emptyList()),
            recordEntry = recordEntry,
            status = taskStatus ?: TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when click event is sent for SingleTask in 'completed' state, then delete record use case is invoked`() {
        val targetTask = buildSingleTaskWithExtrasAndRecordInCompletedState()
        val targetDate = viewModel.uiScreenState.value.currentDate
        runOnTaskClickEventTest(
            targetTaskWithExtrasAndRecord = targetTask,
            doMock = {
                mockDeleteRecordUseCaseForTask(
                    taskId = targetTask.task.id,
                    date = targetDate
                )
            },
            doVerify = {
                verifyDeleteRecordUseCaseCalledOnceForTask(
                    taskId = targetTask.task.id,
                    date = targetDate
                )
            }
        )
    }

    private fun buildSingleTaskWithExtrasAndRecordInCompletedState(): TaskWithExtrasAndRecordModel.Task.SingleTask {
        return buildTestSingleTaskWithExtrasAndRecord(
            recordEntry = null,
            taskStatus = TaskStatus.Completed
        )
    }

    private fun mockDeleteRecordUseCaseForTask(taskId: String, date: LocalDate) {
        wheneverBlocking {
            deleteRecordUseCase.invoke(
                taskId = taskId,
                date = date
            )
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun verifyDeleteRecordUseCaseCalledOnceForTask(taskId: String, date: LocalDate) {
        verifyBlocking(deleteRecordUseCase) {
            invoke(taskId = taskId, date = date)
        }
    }

    @Test
    fun `when click event is sent for SingleTask in 'pending' state, then appropriate use case is invoked`() {
        val targetTask = buildTestPendingSingleTaskWithExtrasAndRecord()
        val targetDate = viewModel.uiScreenState.value.currentDate
        runOnTaskClickEventTest(
            targetTaskWithExtrasAndRecord = targetTask,
            doMock = {
                mockSaveTaskRecordForPendingSingleTask(
                    task = targetTask.task,
                    date = targetDate
                )
            },
            doVerify = {
                verifySaveTaskRecordForPendingSingleTaskCalledOnce(
                    task = targetTask.task,
                    date = targetDate
                )
            }
        )
    }

    private fun buildTestPendingSingleTaskWithExtrasAndRecord(): TaskWithExtrasAndRecordModel.Task.SingleTask {
        return buildTestSingleTaskWithExtrasAndRecord(
            recordEntry = null,
            taskStatus = TaskStatus.NotCompleted.Pending
        )
    }

    private fun mockSaveTaskRecordForPendingSingleTask(
        task: TaskModel.Task.SingleTask,
        date: LocalDate
    ) {
        wheneverBlocking {
            saveRecordUseCase(
                taskId = task.id,
                date = date,
                requestType = SaveRecordUseCase.RequestType.EntryDone
            )
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun sendOnTaskClickEventForTask(taskId: String) {
        viewModel.onEvent(ViewScheduleScreenEvent.OnTaskClick(taskId))
    }

    private fun verifySaveTaskRecordForPendingSingleTaskCalledOnce(
        task: TaskModel.Task.SingleTask,
        date: LocalDate
    ) {
        verifyBlocking(saveRecordUseCase) {
            invoke(
                taskId = task.id,
                date = date,
                requestType = SaveRecordUseCase.RequestType.EntryDone
            )
        }
    }

    private fun buildTestSingleTaskWithExtrasAndRecord(
        recordEntry: RecordEntry.TaskEntry? = null,
        taskStatus: TaskStatus.Task? = null,
        extras: TaskExtras.Task.SingleTask? = null
    ): TaskWithExtrasAndRecordModel.Task.SingleTask {
        return TaskWithExtrasAndRecordModel.Task.SingleTask(
            task = SingleTaskFactory().build(),
            taskExtras = extras ?: TaskExtras.Task.SingleTask(emptyList()),
            recordEntry = recordEntry,
            status = taskStatus ?: TaskStatus.NotCompleted.Pending
        )
    }

    private fun runOnTaskClickEventTest(
        targetTaskWithExtrasAndRecord: TaskWithExtrasAndRecordModel,
        doMock: suspend () -> Unit,
        doVerify: suspend () -> Unit
    ) = runLocalTest {
        collectUIState()
        fillReadTasksUseCaseWithTasks(listOf(targetTaskWithExtrasAndRecord))
        advanceUntilIdle()
        doMock()
        sendOnTaskClickEventForTask(targetTaskWithExtrasAndRecord.task.id)
        advanceUntilIdle()
        doVerify()
    }

    @Test
    fun `when task action 'done' is sent, then appropriate use case is invoked`() = runLocalTest {
        collectUIState()
        val targetTask = buildTestHabitYesNoWithExtrasAndRecord()
        fillReadTasksUseCaseWithTasks(listOf(targetTask))
        val result = buildDoneActionResultForTask(targetTask.task)
        mockSaveTaskRecordForResult(result)
        sendOnTaskActionResultEvent(result)
        advanceUntilIdle()
        verifySaveRecordUseCaseCalledOnceForResult(result)
    }

    private fun buildDoneActionResultForTask(task: TaskModel.Habit.HabitYesNo): ViewTaskActionsScreenResult.OnActionClick.Done {
        return ViewTaskActionsScreenResult.OnActionClick.Done(
            taskId = task.id,
            date = TestUtil.buildRandomDate()
        )
    }

    private fun mockSaveTaskRecordForResult(result: ViewTaskActionsScreenResult.OnActionClick.Done) {
        wheneverBlocking {
            saveRecordUseCase.invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryDone
            )
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun verifySaveRecordUseCaseCalledOnceForResult(result: ViewTaskActionsScreenResult.OnActionClick.Done) {
        verifyBlocking(saveRecordUseCase) {
            invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryDone
            )
        }
    }

    @Test
    fun `when task action 'skip' is sent, then appropriate use case is invoked`() = runLocalTest {
        collectUIState()
        val targetTask = buildTestHabitYesNoWithExtrasAndRecord()
        fillReadTasksUseCaseWithTasks(listOf(targetTask))
        val result = buildSkipActionResultForTask(targetTask.task)
        mockSaveTaskRecordForResult(result)
        sendOnTaskActionResultEvent(result)
        advanceUntilIdle()
        verifySaveRecordUseCaseCalledOnceForResult(result)
    }

    private fun buildSkipActionResultForTask(task: TaskModel.Habit): ViewTaskActionsScreenResult.OnActionClick.Skip {
        return ViewTaskActionsScreenResult.OnActionClick.Skip(
            taskId = task.id,
            date = TestUtil.buildRandomDate()
        )
    }

    private fun mockSaveTaskRecordForResult(result: ViewTaskActionsScreenResult.OnActionClick.Skip) {
        wheneverBlocking {
            saveRecordUseCase.invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntrySkip
            )
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun verifySaveRecordUseCaseCalledOnceForResult(result: ViewTaskActionsScreenResult.OnActionClick.Skip) {
        verifyBlocking(saveRecordUseCase) {
            invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntrySkip
            )
        }
    }

    @Test
    fun `when task action 'fail' is sent, then appropriate use case is invoked`() = runLocalTest {
        collectUIState()
        val targetTask = buildTestHabitYesNoWithExtrasAndRecord()
        fillReadTasksUseCaseWithTasks(listOf(targetTask))
        val result = buildFailActionResultForTask(targetTask.task)
        mockSaveTaskRecordForResult(result)
        sendOnTaskActionResultEvent(result)
        advanceUntilIdle()
        verifySaveRecordUseCaseCalledOnceForResult(result)
    }

    private fun buildFailActionResultForTask(task: TaskModel.Habit): ViewTaskActionsScreenResult.OnActionClick.Fail {
        return ViewTaskActionsScreenResult.OnActionClick.Fail(
            taskId = task.id,
            date = TestUtil.buildRandomDate()
        )
    }

    private fun mockSaveTaskRecordForResult(result: ViewTaskActionsScreenResult.OnActionClick.Fail) {
        wheneverBlocking {
            saveRecordUseCase.invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryFail
            )
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun verifySaveRecordUseCaseCalledOnceForResult(result: ViewTaskActionsScreenResult.OnActionClick.Fail) {
        verifyBlocking(saveRecordUseCase) {
            invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryFail
            )
        }
    }

    @Test
    fun `when task action 'reset' is sent, then appropriate use case is invoked`() = runLocalTest {
        collectUIState()
        val targetTask = buildTestHabitYesNoWithExtrasAndRecord()
        fillReadTasksUseCaseWithTasks(listOf(targetTask))
        val result = buildResetEntryResultForTask(targetTask.task)
        mockDeleteRecordUseCaseForResult(result)
        sendOnTaskActionResultEvent(result)
        advanceUntilIdle()
        verifyDeleteRecordUseCaseCalledOnceForResult(result)
    }

    private fun buildResetEntryResultForTask(task: TaskModel): ViewTaskActionsScreenResult.OnActionClick.ResetEntry {
        return ViewTaskActionsScreenResult.OnActionClick.ResetEntry(
            taskId = task.id,
            date = TestUtil.buildRandomDate()
        )
    }

    private fun mockDeleteRecordUseCaseForResult(result: ViewTaskActionsScreenResult.OnActionClick.ResetEntry) {
        wheneverBlocking {
            deleteRecordUseCase.invoke(
                taskId = result.taskId,
                date = result.date
            )
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun sendOnTaskActionResultEvent(result: ViewTaskActionsScreenResult.OnActionClick) {
        viewModel.onEvent(ViewScheduleScreenEvent.ResultEvent.ViewTaskActions(result))
    }

    private fun verifyDeleteRecordUseCaseCalledOnceForResult(result: ViewTaskActionsScreenResult.OnActionClick.ResetEntry) {
        verifyBlocking(deleteRecordUseCase) {
            deleteRecordUseCase.invoke(
                taskId = result.taskId,
                date = result.date
            )
        }
    }

    private fun buildTestHabitYesNoWithExtrasAndRecord(
        recordEntry: RecordEntry.HabitEntry.YesNo? = null,
        taskStatus: TaskStatus.Habit? = null,
        extras: TaskExtras.Habit.HabitYesNo? = null
    ): TaskWithExtrasAndRecordModel.Habit.HabitYesNo {
        return TaskWithExtrasAndRecordModel.Habit.HabitYesNo(
            task = HabitYesNoFactory().build(),
            taskExtras = extras ?: TaskExtras.Habit.HabitYesNo(emptyList()),
            recordEntry = recordEntry,
            status = taskStatus ?: TaskStatus.NotCompleted.Pending
        )
    }


    @Test
    fun `when enter time record is confirmed, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildTestHabitTimeWithExtrasAndRecord()
            fillReadTasksUseCaseWithTasks(listOf(targetTask))
            collectUIState()
            val confirmResult = buildEnterTaskTimeRecordConfirmResultForTask(targetTask.task)
            mockSaveRecordUseCaseForResult(confirmResult)
            sendEnterTimeRecordEventWithConfirmResult(confirmResult)
            advanceUntilIdle()
            verifySaveRecordUseCaseCalledOnceWithResult(confirmResult)
        }

    private fun buildTestHabitTimeWithExtrasAndRecord(
        recordEntry: RecordEntry.HabitEntry.Continuous.Time? = null,
        taskStatus: TaskStatus.Habit? = null,
        extras: TaskExtras.Habit.HabitContinuous.HabitTime? = null
    ): TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime {
        return TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitTime(
            task = HabitTimeFactory().build(),
            taskExtras = extras ?: TaskExtras.Habit.HabitContinuous.HabitTime(emptyList()),
            recordEntry = recordEntry,
            status = taskStatus ?: TaskStatus.NotCompleted.Pending
        )
    }

    private fun buildEnterTaskTimeRecordConfirmResultForTask(task: TaskModel.Habit.HabitContinuous.HabitTime): EnterTaskTimeRecordScreenResult.Confirm {
        return EnterTaskTimeRecordScreenResult.Confirm(
            taskId = task.id,
            date = task.date.startDate,
            time = TestUtil.buildRandomLocalTime()
        )
    }

    private fun mockSaveRecordUseCaseForResult(result: EnterTaskTimeRecordScreenResult.Confirm) {
        wheneverBlocking {
            saveRecordUseCase.invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryTime(result.time)
            )
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun sendEnterTimeRecordEventWithConfirmResult(result: EnterTaskTimeRecordScreenResult.Confirm) {
        viewModel.onEvent(ViewScheduleScreenEvent.ResultEvent.EnterTaskTimeRecord(result))
    }

    private fun verifySaveRecordUseCaseCalledOnceWithResult(result: EnterTaskTimeRecordScreenResult.Confirm) {
        verifyBlocking(saveRecordUseCase) {
            invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryTime(result.time)
            )
        }
    }

    @Test
    fun `when enter number record is confirmed, then appropriate use case is invoked`() =
        runLocalTest {
            collectUIState()
            val targetTaskWithExtrasAndRecord = buildTestHabitNumberWithExtrasAndRecord()
            val confirmResult = buildConfirmEnterNumberRecordResultForTask(
                targetTaskWithExtrasAndRecord.task
            )
            fillReadTasksUseCaseWithTasks(listOf(targetTaskWithExtrasAndRecord))
            mockSaveRecordUseCaseForResult(confirmResult)
            advanceUntilIdle()
            sendEnterTaskNumberRecordConfirmEvent(confirmResult)
            advanceUntilIdle()
            verifySaveRecordUseCaseCalledOnceWithResult(confirmResult)
        }

    private fun buildConfirmEnterNumberRecordResultForTask(targetTask: TaskModel.Habit.HabitContinuous.HabitNumber): EnterTaskNumberRecordScreenResult.Confirm {
        return EnterTaskNumberRecordScreenResult.Confirm(
            taskId = targetTask.id,
            date = targetTask.date.startDate,
            number = 0.0
        )
    }

    private fun mockSaveRecordUseCaseForResult(result: EnterTaskNumberRecordScreenResult.Confirm) {
        wheneverBlocking {
            saveRecordUseCase.invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryNumber(result.number)
            )
        }.thenReturn(ResultModel.success(Unit))
    }

    private fun sendEnterTaskNumberRecordConfirmEvent(result: EnterTaskNumberRecordScreenResult.Confirm) {
        viewModel.onEvent(
            ViewScheduleScreenEvent.ResultEvent.EnterTaskNumberRecord(result)
        )
    }

    private fun verifySaveRecordUseCaseCalledOnceWithResult(result: EnterTaskNumberRecordScreenResult.Confirm) {
        verifyBlocking(saveRecordUseCase) {
            invoke(
                taskId = result.taskId,
                date = result.date,
                requestType = SaveRecordUseCase.RequestType.EntryNumber(result.number)
            )
        }
    }

    private fun buildTestHabitNumberWithExtrasAndRecord(
        recordEntry: RecordEntry.HabitEntry.Continuous.Number? = null,
        taskStatus: TaskStatus.Habit? = null,
        extras: TaskExtras.Habit.HabitContinuous.HabitNumber? = null
    ): TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber {
        return TaskWithExtrasAndRecordModel.Habit.HabitContinuous.HabitNumber(
            task = HabitNumberFactory().build(),
            taskExtras = extras ?: TaskExtras.Habit.HabitContinuous.HabitNumber(emptyList()),
            recordEntry = recordEntry,
            status = taskStatus ?: TaskStatus.NotCompleted.Pending
        )
    }

    @Test
    fun `when pick date is confirmed, then current date is updated correctly`() = runLocalTest {
        collectUIState()
        val confirmResult = buildPickDateConfirmResult()
        sendPickDateEventWithConfirmResult(confirmResult)
        advanceUntilIdle()
        assertThat(viewModel.uiScreenState.value.currentDate).isEqualTo(confirmResult.date)
    }

    private fun buildPickDateConfirmResult(): PickDateScreenResult.Confirm {
        return PickDateScreenResult.Confirm(TestUtil.buildRandomDate())
    }

    private fun sendPickDateEventWithConfirmResult(result: PickDateScreenResult.Confirm) {
        viewModel.onEvent(ViewScheduleScreenEvent.ResultEvent.PickDate(result))
    }

    private fun fillReadTasksUseCaseWithTasks(data: List<TaskWithExtrasAndRecordModel>) {
        fakeReadTasksWithExtrasAndRecordByDateUseCase.dataState.update { data }
    }

    private fun TestScope.collectUIState() {
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun initDependencies() {
        fakeReadTasksWithExtrasAndRecordByDateUseCase =
            FakeReadTasksWithExtrasAndRecordByDateUseCase()
        spySortTasksUseCase = spy(DefaultSortTasksUseCase())
        testDispatcher = StandardTestDispatcher()
    }

    private fun initViewModel() {
        viewModel = ViewScheduleViewModel(
            readTasksWithExtrasAndRecordByDateUseCase = fakeReadTasksWithExtrasAndRecordByDateUseCase,
            saveTaskDraftUseCase = saveTaskDraftUseCase,
            saveRecordUseCase = saveRecordUseCase,
            deleteRecordUseCase = deleteRecordUseCase,
            defaultDispatcher = testDispatcher,
            viewModelScope = TestScope(testDispatcher),
            sortTasksUseCase = spySortTasksUseCase
        )
    }

}

private class FakeReadTasksWithExtrasAndRecordByDateUseCase :
    ReadTasksWithExtrasAndRecordByDateUseCase {
    val dataState = MutableStateFlow<List<TaskWithExtrasAndRecordModel>>(emptyList())
    override fun invoke(date: LocalDate): Flow<List<TaskWithExtrasAndRecordModel>> {
        return dataState
    }
}








