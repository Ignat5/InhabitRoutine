package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base

import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.presentation.components.config.ScreenConfig
import com.ignatlegostaev.inhabitroutine.core.presentation.components.event.ScreenEvent
import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.ScreenNavigation
import com.ignatlegostaev.inhabitroutine.core.presentation.components.state.ScreenState
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitNumberFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitTimeFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.RecurringTaskFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskPriorityByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenNavigation
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.wheneverBlocking

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class BaseCreateEditTaskViewModelTest {

    private lateinit var fakeBaseCreateEditTaskViewModel: FakeBaseCreateEditTaskViewModel
    private lateinit var testDispatcher: TestDispatcher

    @Mock
    private lateinit var updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase

    @Mock
    private lateinit var updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase

    @Mock
    private lateinit var updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase

    @Mock
    private lateinit var updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase

    @Mock
    private lateinit var updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase

    @Mock
    private lateinit var updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase

    @Before
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        fakeBaseCreateEditTaskViewModel = FakeBaseCreateEditTaskViewModel(
            updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
            updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
            updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
            updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
            updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
            updateTaskPriorityByIdUseCase = updateTaskPriorityByIdUseCase,
            defaultDispatcher = testDispatcher,
            viewModelScope = TestScope(testDispatcher)
        )
    }

    @Test
    fun `when confirm RecurringTask end date event is sent, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildRecurringTask()
            setUpTaskModelState(targetTask)
            val confirmResult = PickDateScreenResult.Confirm(TestUtil.buildRandomDate())
            wheneverBlocking {
                updateTaskDateByIdUseCase(
                    taskId = targetTask.id, taskDate = TaskDate.Period(
                        startDate = targetTask.date.startDate,
                        endDate = confirmResult.date
                    )
                )
            }.thenReturn(ResultModel.success(Unit))
            sendBaseEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.EndDate(confirmResult))
            advanceUntilIdle()
            verify(updateTaskDateByIdUseCase).invoke(
                taskId = targetTask.id,
                taskDate = TaskDate.Period(
                    startDate = targetTask.date.startDate,
                    endDate = confirmResult.date
                )
            )
        }

    @Test
    fun `when confirm RecurringTask start date event is sent, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildRecurringTask()
            setUpTaskModelState(targetTask)
            val confirmResult = PickDateScreenResult.Confirm(TestUtil.buildRandomDate())
            wheneverBlocking {
                updateTaskDateByIdUseCase(
                    taskId = targetTask.id, taskDate = TaskDate.Period(
                        startDate = confirmResult.date,
                        endDate = targetTask.date.endDate
                    )
                )
            }.thenReturn(ResultModel.success(Unit))
            sendBaseEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.StartDate(confirmResult))
            advanceUntilIdle()
            verify(updateTaskDateByIdUseCase).invoke(
                taskId = targetTask.id,
                taskDate = TaskDate.Period(
                    startDate = confirmResult.date,
                    endDate = targetTask.date.endDate
                )
            )
        }

    @Test
    fun `when confirm SingleTask date event is sent, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildSingleTask()
            setUpTaskModelState(targetTask)
            val confirmResult = PickDateScreenResult.Confirm(TestUtil.buildRandomDate())
            wheneverBlocking {
                updateTaskDateByIdUseCase(
                    taskId = targetTask.id,
                    taskDate = TaskDate.Day(confirmResult.date)
                )
            }.thenReturn(ResultModel.success(Unit))
            sendBaseEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.Date(confirmResult))
            advanceUntilIdle()
            verify(updateTaskDateByIdUseCase).invoke(
                taskId = targetTask.id,
                taskDate = TaskDate.Day(confirmResult.date)
            )
        }

    @Test
    fun `when confirm task time progress event is sent, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildHabitTime()
            setUpTaskModelState(targetTask)
            val confirmResult = PickTaskTimeProgressScreenResult.Confirm(
                TestUtil.buildRandomTaskProgressTime()
            )
            wheneverBlocking {
                updateTaskProgressByIdUseCase(
                    taskId = targetTask.id,
                    taskProgress = confirmResult.taskProgress
                )
            }.thenReturn(ResultModel.success(Unit))
            sendBaseEvent(
                BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTimeProgress(
                    confirmResult
                )
            )
            advanceUntilIdle()
            verify(updateTaskProgressByIdUseCase).invoke(
                taskId = targetTask.id,
                taskProgress = confirmResult.taskProgress
            )
        }

    @Test
    fun `when confirm task number progress event is sent, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildHabitNumber()
            setUpTaskModelState(targetTask)
            val confirmResult = PickTaskNumberProgressScreenResult.Confirm(
                TestUtil.buildRandomTaskNumberProgress()
            )
            wheneverBlocking {
                updateTaskProgressByIdUseCase(
                    taskId = targetTask.id,
                    taskProgress = confirmResult.taskProgress
                )
            }.thenReturn(ResultModel.success(Unit))
            sendBaseEvent(
                BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskNumberProgress(
                    confirmResult
                )
            )
            advanceUntilIdle()
            verify(updateTaskProgressByIdUseCase).invoke(
                taskId = targetTask.id,
                taskProgress = confirmResult.taskProgress
            )
        }

    @Test
    fun `when confirm task frequency event is sent, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildRecurringTask()
            setUpTaskModelState(targetTask)
            val confirmResult = PickTaskFrequencyScreenResult.Confirm(TaskFrequency.EveryDay)
            wheneverBlocking {
                updateTaskFrequencyByIdUseCase(
                    taskId = targetTask.id,
                    taskFrequency = confirmResult.taskFrequency
                )
            }.thenReturn(ResultModel.success(Unit))
            sendBaseEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskFrequency(confirmResult))
            advanceUntilIdle()
            verify(updateTaskFrequencyByIdUseCase).invoke(
                taskId = targetTask.id,
                taskFrequency = confirmResult.taskFrequency
            )
        }

    @Test
    fun `when confirm task priority event is sent, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildSingleTask()
            setUpTaskModelState(targetTask)
            val confirmResult = PickTaskPriorityScreenResult.Confirm(5)
            wheneverBlocking {
                updateTaskPriorityByIdUseCase(
                    taskId = targetTask.id,
                    priority = confirmResult.priority
                )
            }.thenReturn(ResultModel.success(Unit))
            sendBaseEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskPriority(confirmResult))
            advanceUntilIdle()
            verify(updateTaskPriorityByIdUseCase).invoke(
                taskId = targetTask.id,
                priority = confirmResult.priority
            )
        }

    @Test
    fun `when confirm task description event is sent, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildSingleTask()
            setUpTaskModelState(targetTask)
            val confirmResult = PickTaskDescriptionScreenResult.Confirm("test desc")
            wheneverBlocking {
                updateTaskDescriptionByIdUseCase(
                    taskId = targetTask.id,
                    description = confirmResult.description
                )
            }.thenReturn(ResultModel.success(Unit))
            sendBaseEvent(
                BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDescription(
                    confirmResult
                )
            )
            advanceUntilIdle()
            verify(updateTaskDescriptionByIdUseCase).invoke(
                taskId = targetTask.id,
                description = confirmResult.description
            )
        }

    @Test
    fun `when confirm task title event is sent, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = buildSingleTask()
            setUpTaskModelState(targetTask)
            val confirmResult = PickTaskTitleScreenResult.Confirm("test title")
            wheneverBlocking {
                updateTaskTitleByIdUseCase(taskId = targetTask.id, title = confirmResult.title)
            }.thenReturn(ResultModel.success(Unit))
            sendBaseEvent(BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle(confirmResult))
            advanceUntilIdle()
            verify(updateTaskTitleByIdUseCase).invoke(
                taskId = targetTask.id,
                title = confirmResult.title
            )
        }

    @Test
    fun `when task is HabitTime, then task config items are aligned with task type`() {
        val targetTask = buildHabitTime()
        val reminderCount = 0
        checkTaskConfigItemsMatch(
            targetTask = targetTask,
            reminderCount = reminderCount,
            expectedConfigItems = setOf(
                BaseItemTaskConfig.Title(targetTask.title),
                BaseItemTaskConfig.Description(targetTask.description),
                BaseItemTaskConfig.Priority(targetTask.priority),
                BaseItemTaskConfig.Frequency(targetTask.frequency),
                BaseItemTaskConfig.Progress.Time(targetTask.progress),
                BaseItemTaskConfig.DateConfig.StartDate(targetTask.date.startDate),
                BaseItemTaskConfig.DateConfig.EndDate(targetTask.date.endDate),
                BaseItemTaskConfig.Reminders(reminderCount),
            )
        )
    }

    @Test
    fun `when task is HabitNumber, then task config items are aligned with task type`() {
        val targetTask = buildHabitNumber()
        val reminderCount = 0
        checkTaskConfigItemsMatch(
            targetTask = targetTask,
            reminderCount = reminderCount,
            expectedConfigItems = setOf(
                BaseItemTaskConfig.Title(targetTask.title),
                BaseItemTaskConfig.Description(targetTask.description),
                BaseItemTaskConfig.Priority(targetTask.priority),
                BaseItemTaskConfig.Frequency(targetTask.frequency),
                BaseItemTaskConfig.Progress.Number(targetTask.progress),
                BaseItemTaskConfig.DateConfig.StartDate(targetTask.date.startDate),
                BaseItemTaskConfig.DateConfig.EndDate(targetTask.date.endDate),
                BaseItemTaskConfig.Reminders(reminderCount),
            )
        )
    }

    @Test
    fun `when task is HabitYesNo, then task config items are aligned with task type`() {
        val targetTask = buildHabitYesNo()
        val reminderCount = 0
        checkTaskConfigItemsMatch(
            targetTask = targetTask,
            reminderCount = reminderCount,
            expectedConfigItems = setOf(
                BaseItemTaskConfig.Title(targetTask.title),
                BaseItemTaskConfig.Description(targetTask.description),
                BaseItemTaskConfig.Priority(targetTask.priority),
                BaseItemTaskConfig.Frequency(targetTask.frequency),
                BaseItemTaskConfig.DateConfig.StartDate(targetTask.date.startDate),
                BaseItemTaskConfig.DateConfig.EndDate(targetTask.date.endDate),
                BaseItemTaskConfig.Reminders(reminderCount),
            )
        )
    }

    @Test
    fun `when task is RecurringTask, then task config items are aligned with task type`() {
        val targetTask = buildRecurringTask()
        val reminderCount = 0
        checkTaskConfigItemsMatch(
            targetTask = targetTask,
            reminderCount = reminderCount,
            expectedConfigItems = setOf(
                BaseItemTaskConfig.Title(targetTask.title),
                BaseItemTaskConfig.Description(targetTask.description),
                BaseItemTaskConfig.Priority(targetTask.priority),
                BaseItemTaskConfig.Frequency(targetTask.frequency),
                BaseItemTaskConfig.DateConfig.StartDate(targetTask.date.startDate),
                BaseItemTaskConfig.DateConfig.EndDate(targetTask.date.endDate),
                BaseItemTaskConfig.Reminders(reminderCount),
            )
        )
    }

    @Test
    fun `when task is SingleTask, then task config items are aligned with task type`() {
        val targetTask = buildSingleTask()
        val reminderCount = 0
        checkTaskConfigItemsMatch(
            targetTask = targetTask,
            reminderCount = reminderCount,
            expectedConfigItems = setOf(
                BaseItemTaskConfig.Title(targetTask.title),
                BaseItemTaskConfig.Description(targetTask.description),
                BaseItemTaskConfig.Priority(targetTask.priority),
                BaseItemTaskConfig.DateConfig.Date(targetTask.date.date),
                BaseItemTaskConfig.Reminders(reminderCount),
            )
        )
    }

    private fun checkTaskConfigItemsMatch(
        targetTask: TaskModel,
        reminderCount: Int,
        expectedConfigItems: Collection<BaseItemTaskConfig>
    ) {
        fakeBaseCreateEditTaskViewModel.provideBaseTaskConfigItems(
            taskModel = targetTask,
            reminderCount = reminderCount
        ).apply {
            assertThat(this).containsNoDuplicates()
            assertThat(this).containsExactlyElementsIn(expectedConfigItems)
        }
    }

    private fun setUpTaskModelState(taskModel: TaskModel) {
        fakeBaseCreateEditTaskViewModel.taskModelState.update { taskModel }
    }

    private fun buildSingleTask(): TaskModel.Task.SingleTask {
        return SingleTaskFactory().build()
    }

    private fun buildRecurringTask(): TaskModel.Task.RecurringTask {
        return RecurringTaskFactory().build()
    }

    private fun buildHabitYesNo(): TaskModel.Habit.HabitYesNo {
        return HabitYesNoFactory().build()
    }

    private fun buildHabitNumber(): TaskModel.Habit.HabitContinuous.HabitNumber {
        return HabitNumberFactory().build()
    }

    private fun buildHabitTime(): TaskModel.Habit.HabitContinuous.HabitTime {
        return HabitTimeFactory().build()
    }

    private fun sendBaseEvent(event: BaseCreateEditTaskScreenEvent) {
        fakeBaseCreateEditTaskViewModel.onEvent(
            FakeScreenEvent(event)
        )
    }

    private fun TestScope.collectUIState() {
        this.backgroundScope.launch {
            fakeBaseCreateEditTaskViewModel.uiScreenState.launchIn(this)
        }
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) {
        runTest(testDispatcher) {
            block()
        }
    }

}

private class FakeBaseCreateEditTaskViewModel(
    updateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase,
    defaultDispatcher: CoroutineDispatcher,
    override val viewModelScope: CoroutineScope
) : BaseCreateEditTaskViewModel<FakeScreenEvent, FakeScreenState, FakeScreenNavigation, FakeScreenConfig>(
    updateTaskTitleByIdUseCase = updateTaskTitleByIdUseCase,
    updateTaskProgressByIdUseCase = updateTaskProgressByIdUseCase,
    updateTaskFrequencyByIdUseCase = updateTaskFrequencyByIdUseCase,
    updateTaskDateByIdUseCase = updateTaskDateByIdUseCase,
    updateTaskDescriptionByIdUseCase = updateTaskDescriptionByIdUseCase,
    updateTaskPriorityByIdUseCase = updateTaskPriorityByIdUseCase,
    defaultDispatcher = defaultDispatcher
) {

    public override val taskModelState: MutableStateFlow<TaskModel?> = MutableStateFlow(null)

    override val uiScreenState: StateFlow<FakeScreenState> = MutableStateFlow(FakeScreenState)

    override fun onEvent(event: FakeScreenEvent) {
        onBaseEvent(event.baseEvent)
    }

    public override fun provideBaseTaskConfigItems(
        taskModel: TaskModel,
        reminderCount: Int
    ): List<BaseItemTaskConfig> {
        return super.provideBaseTaskConfigItems(taskModel, reminderCount)
    }

    public override fun setUpBaseConfigState(baseConfig: BaseCreateEditTaskScreenConfig) {}

    public override fun setUpBaseNavigationState(baseNavigation: BaseCreateEditTaskScreenNavigation) {}

}

private data class FakeScreenEvent(
    val baseEvent: BaseCreateEditTaskScreenEvent
) : ScreenEvent

private object FakeScreenState : ScreenState
private object FakeScreenNavigation : ScreenNavigation
private object FakeScreenConfig : ScreenConfig