package com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create

import com.ignatlegostaev.inhabitroutine.core.test.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadReminderCountByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDateByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskFrequencyByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskPriorityByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.pick_date.components.PickDateScreenResult
import com.ignatlegostaev.inhabitroutine.core.test.HabitNumberFactory
import com.ignatlegostaev.inhabitroutine.core.test.HabitTimeFactory
import com.ignatlegostaev.inhabitroutine.core.test.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.test.RecurringTaskFactory
import com.ignatlegostaev.inhabitroutine.core.test.TaskAbstractFactory
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskFrequency
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.model.task.type.ProgressLimitType
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.components.BaseCreateEditTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_description.components.PickTaskDescriptionScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_frequency.components.PickTaskFrequencyScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_number_progress.components.PickTaskNumberProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_priority.components.PickTaskPriorityScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_time_progress.components.PickTaskTimeProgressScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.config.pick_task_title.components.PickTaskTitleScreenResult
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.base.model.BaseItemTaskConfig
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.components.CreateTaskScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.create_edit_task.create.other.VerifyCanSaveTaskUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.wheneverBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTaskViewModelTest {

    private lateinit var viewModel: CreateTaskViewModel
    private lateinit var taskId: String
    private lateinit var fakeReadTaskByIdUseCase: FakeReadTaskByIdUseCase
    private lateinit var fakeReadReminderCountByTaskIdUseCase: FakeReadReminderCountByTaskIdUseCase
    private lateinit var mockedSaveTaskByIdUseCase: SaveTaskByIdUseCase
    private lateinit var mockedDeleteTaskByIdUseCase: DeleteTaskByIdUseCase
    private lateinit var mockedUpdateTaskTitleByIdUseCase: UpdateTaskTitleByIdUseCase
    private lateinit var mockedUpdateTaskProgressByIdUseCase: UpdateTaskProgressByIdUseCase
    private lateinit var mockedUpdateTaskFrequencyByIdUseCase: UpdateTaskFrequencyByIdUseCase
    private lateinit var mockedUpdateTaskDateByIdUseCase: UpdateTaskDateByIdUseCase
    private lateinit var mockedUpdateTaskDescriptionByIdUseCase: UpdateTaskDescriptionByIdUseCase
    private lateinit var mockedUpdateTaskPriorityByIdUseCase: UpdateTaskPriorityByIdUseCase
    private lateinit var mockedVerifyCanSaveTaskUseCase: VerifyCanSaveTaskUseCase
    private lateinit var testDispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        initDependencies()
        initViewModel()
    }

    @Test
    fun `when leaving screen without saving the task, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = SingleTaskFactory().build().copy(id = taskId)
            whenever(mockedVerifyCanSaveTaskUseCase.invoke(targetTask)).thenReturn(false)
            runOnEventVerifyMockTest(
                targetTask = targetTask,
                onEvent = {
                    CreateTaskScreenEvent.OnLeaveRequest
                },
                mock = mockedDeleteTaskByIdUseCase,
                doCall = {
                    invoke(targetTask.id)
                },
                mockedResult = ResultModel.success(Unit)
            )
        }

    @Test
    fun `when trying to save task that passes canSave check, then appropriate use case is invoked`() =
        runLocalTest {
            val targetTask = SingleTaskFactory().build().copy(id = taskId)
            fakeReadTaskByIdUseCase.taskState.update { targetTask }
            whenever(mockedVerifyCanSaveTaskUseCase.invoke(any())).thenReturn(true)
            collectUIState()
            advanceUntilIdle()
            assertThat(viewModel.uiScreenState.value.canSave).isTrue()
            runOnEventVerifyMockTest(
                targetTask = targetTask,
                onEvent = {
                    CreateTaskScreenEvent.OnSaveClick
                },
                mock = mockedSaveTaskByIdUseCase,
                doCall = {
                    invoke(targetTask.id)
                },
                mockedResult = ResultModel.success(Unit)
            )
        }

    @Test
    fun `when trying to save task that doesn't pass canSave check, then appropriate use case is not invoked`() {
        val targetTask = SingleTaskFactory().build().copy(id = taskId)
        fakeReadTaskByIdUseCase.taskState.update { targetTask }
        whenever(mockedVerifyCanSaveTaskUseCase.invoke(any())).thenReturn(false)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.OnSaveClick
            },
            mock = mockedSaveTaskByIdUseCase,
            doCall = {
                invoke(targetTask.id)
            },
            mockedResult = ResultModel.success(Unit),
            expectedTimes = 0
        )
    }

    @Test
    fun `when end date is picked for recurring task, appropriate use case is invoked`() {
        val confirmResult = PickDateScreenResult.Confirm(
            date = LocalDate(
                year = 2020,
                monthNumber = 1,
                dayOfMonth = 1
            )
        )
        val targetTask = RecurringTaskFactory().build().copy(
            id = taskId,
            date = TaskDate.Period(
                startDate = confirmResult.date.minus(1, DateTimeUnit.DAY),
                endDate = confirmResult.date.plus(1, DateTimeUnit.DAY)
            )
        )
        assertThat(targetTask.date.endDate).isNotEqualTo(confirmResult.date)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.Base(
                    BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.EndDate(
                        result = confirmResult
                    )
                )
            },
            mock = mockedUpdateTaskDateByIdUseCase,
            doCall = {
                invoke(
                    taskId = targetTask.id, taskDate = TaskDate.Period(
                        startDate = targetTask.date.startDate,
                        endDate = confirmResult.date
                    )
                )
            },
            mockedResult = ResultModel.success(Unit)
        )
    }

    @Test
    fun `when start date is picked for recurring task, appropriate use case is invoked`() {
        val confirmResult = PickDateScreenResult.Confirm(
            date = LocalDate(
                year = 2020,
                monthNumber = 1,
                dayOfMonth = 1
            )
        )
        val targetTask = RecurringTaskFactory().build().copy(
            id = taskId,
            date = TaskDate.Period(
                startDate = confirmResult.date.minus(1, DateTimeUnit.DAY),
                endDate = confirmResult.date.plus(1, DateTimeUnit.DAY)
            )
        )
        assertThat(targetTask.date.startDate).isNotEqualTo(confirmResult.date)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.Base(
                    BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.StartDate(
                        result = confirmResult
                    )
                )
            },
            mock = mockedUpdateTaskDateByIdUseCase,
            doCall = {
                invoke(
                    taskId = targetTask.id, taskDate = TaskDate.Period(
                        startDate = confirmResult.date,
                        endDate = targetTask.date.endDate
                    )
                )
            },
            mockedResult = ResultModel.success(Unit)
        )
    }

    @Test
    fun `when date is picked for SingleTask, appropriate use case is invoked`() {
        val confirmResult = PickDateScreenResult.Confirm(
            date = LocalDate(
                year = 2020,
                monthNumber = 1,
                dayOfMonth = 1
            )
        )
        val targetTask = SingleTaskFactory().build().copy(id = taskId)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.Base(
                    BaseCreateEditTaskScreenEvent.ResultEvent.PickDate.Date(
                        result = confirmResult
                    )
                )
            },
            mock = mockedUpdateTaskDateByIdUseCase,
            doCall = {
                invoke(taskId = targetTask.id, taskDate = TaskDate.Day(confirmResult.date))
            },
            mockedResult = ResultModel.success(Unit)
        )
    }

    @Test
    fun `when task time progress is confirmed, appropriate use case is invoked`() {
        val pickedTimeProgress = TaskProgress.Time(
            limitType = ProgressLimitType.AtLeast,
            limitTime = LocalTime(hour = 0, minute = 0)
        )
        val confirmResult = PickTaskTimeProgressScreenResult.Confirm(pickedTimeProgress)
        val targetTask = HabitTimeFactory().build().copy(
            id = taskId,
            progress = pickedTimeProgress.copy(limitTime = LocalTime(hour = 1, minute = 1))
        )
        assertThat(targetTask).isNotEqualTo(pickedTimeProgress)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.Base(
                    BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTimeProgress(confirmResult)
                )
            },
            mock = mockedUpdateTaskProgressByIdUseCase,
            doCall = {
                invoke(taskId = targetTask.id, taskProgress = confirmResult.taskProgress)
            },
            mockedResult = ResultModel.success(Unit)
        )
    }

    @Test
    fun `when task number progress is confirmed, appropriate use case is invoked`() {
        val pickedNumberProgress = TaskProgress.Number(
            limitType = ProgressLimitType.AtLeast,
            limitNumber = 0.0,
            limitUnit = randomUUID()
        )
        val confirmResult = PickTaskNumberProgressScreenResult.Confirm(pickedNumberProgress)
        val targetTask = HabitNumberFactory().build().copy(
            id = taskId,
            progress = pickedNumberProgress.copy(limitUnit = randomUUID())
        )
        assertThat(targetTask.progress).isNotEqualTo(confirmResult.taskProgress)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.Base(
                    BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskNumberProgress(confirmResult)
                )
            },
            mock = mockedUpdateTaskProgressByIdUseCase,
            doCall = {
                invoke(taskId = targetTask.id, taskProgress = confirmResult.taskProgress)
            },
            mockedResult = ResultModel.success(Unit)
        )
    }

    @Test
    fun `when task frequency is confirmed, appropriate use case is invoked`() {
        val confirmResult = PickTaskFrequencyScreenResult.Confirm(TaskFrequency.EveryDay)
        val targetTask = RecurringTaskFactory().build().copy(id = taskId)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.Base(
                    BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskFrequency(confirmResult)
                )
            },
            mock = mockedUpdateTaskFrequencyByIdUseCase,
            doCall = {
                invoke(taskId = targetTask.id, taskFrequency = confirmResult.taskFrequency)
            },
            mockedResult = ResultModel.success(Unit)
        )
    }

    @Test
    fun `when task priority is confirmed, appropriate use case is invoked`() {
        val confirmResult = PickTaskPriorityScreenResult.Confirm(1)
        val targetTask = SingleTaskFactory().build().copy(id = taskId)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.Base(
                    BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskPriority(confirmResult)
                )
            },
            mock = mockedUpdateTaskPriorityByIdUseCase,
            doCall = {
                invoke(taskId = targetTask.id, priority = confirmResult.priority)
            },
            mockedResult = ResultModel.success(Unit)
        )
    }

    @Test
    fun `when task description is confirmed, appropriate use case is invoked`() {
        val confirmResult = PickTaskDescriptionScreenResult.Confirm("test desc")
        val targetTask = SingleTaskFactory().build().copy(id = taskId)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.Base(
                    BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskDescription(confirmResult)
                )
            },
            mock = mockedUpdateTaskDescriptionByIdUseCase,
            doCall = {
                invoke(taskId = targetTask.id, description = confirmResult.description)
            },
            mockedResult = ResultModel.success(Unit)
        )
    }

    @Test
    fun `when task title is confirmed, appropriate use case is invoked`() {
        val confirmResult = PickTaskTitleScreenResult.Confirm("test title")
        val targetTask = SingleTaskFactory().build().copy(id = taskId)
        runOnEventVerifyMockTest(
            targetTask = targetTask,
            onEvent = {
                CreateTaskScreenEvent.Base(
                    BaseCreateEditTaskScreenEvent.ResultEvent.PickTaskTitle(confirmResult)
                )
            },
            mock = mockedUpdateTaskTitleByIdUseCase,
            doCall = {
                invoke(taskId = targetTask.id, title = confirmResult.title)
            },
            mockedResult = ResultModel.success(Unit)
        )
    }

    private fun <T, R> runOnEventVerifyMockTest(
        targetTask: TaskModel,
        onEvent: () -> CreateTaskScreenEvent,
        mock: T,
        doCall: suspend T.() -> R,
        mockedResult: R,
        expectedTimes: Int = 1,
    ) = runLocalTest {
        fakeReadTaskByIdUseCase.taskState.update { targetTask }
        collectUIState()
        advanceUntilIdle()
        wheneverBlocking {
            mock.doCall()
        }.thenReturn(mockedResult)
        viewModel.onEvent(onEvent())
        advanceUntilIdle()
        verify(mock, times(expectedTimes)).apply {
            doCall()
        }
    }

    @Test
    fun `when task is HabitTime, then provided config items match the task`() = runLocalTest {
        runTaskConfigItemsTest(
            taskFactory = HabitTimeFactory(),
            expected = {
                (it as TaskModel.Habit.HabitContinuous.HabitTime).let { targetTask ->
                    setOf(
                        BaseItemTaskConfig.Title(targetTask.title),
                        BaseItemTaskConfig.Description(targetTask.description),
                        BaseItemTaskConfig.Priority(targetTask.priority),
                        BaseItemTaskConfig.Frequency(targetTask.frequency),
                        BaseItemTaskConfig.Progress.Time(targetTask.progress),
                        BaseItemTaskConfig.DateConfig.StartDate(targetTask.date.startDate),
                        BaseItemTaskConfig.DateConfig.EndDate(targetTask.date.endDate),
                        BaseItemTaskConfig.Reminders(fakeReadReminderCountByTaskIdUseCase.reminderCountState.value)
                    )
                }
            }
        )
    }

    @Test
    fun `when task is HabitNumber, then provided config items match the task`() = runLocalTest {
        runTaskConfigItemsTest(
            taskFactory = HabitNumberFactory(),
            expected = {
                (it as TaskModel.Habit.HabitContinuous.HabitNumber).let { targetTask ->
                    setOf(
                        BaseItemTaskConfig.Title(targetTask.title),
                        BaseItemTaskConfig.Description(targetTask.description),
                        BaseItemTaskConfig.Priority(targetTask.priority),
                        BaseItemTaskConfig.Frequency(targetTask.frequency),
                        BaseItemTaskConfig.Progress.Number(targetTask.progress),
                        BaseItemTaskConfig.DateConfig.StartDate(targetTask.date.startDate),
                        BaseItemTaskConfig.DateConfig.EndDate(targetTask.date.endDate),
                        BaseItemTaskConfig.Reminders(fakeReadReminderCountByTaskIdUseCase.reminderCountState.value)
                    )
                }
            }
        )
    }

    @Test
    fun `when task is HabitYesNo, then provided config items match the task`() = runLocalTest {
        runTaskConfigItemsTest(
            taskFactory = HabitYesNoFactory(),
            expected = {
                (it as TaskModel.Habit.HabitYesNo).let { targetTask ->
                    setOf(
                        BaseItemTaskConfig.Title(targetTask.title),
                        BaseItemTaskConfig.Description(targetTask.description),
                        BaseItemTaskConfig.Priority(targetTask.priority),
                        BaseItemTaskConfig.Frequency(targetTask.frequency),
                        BaseItemTaskConfig.DateConfig.StartDate(targetTask.date.startDate),
                        BaseItemTaskConfig.DateConfig.EndDate(targetTask.date.endDate),
                        BaseItemTaskConfig.Reminders(fakeReadReminderCountByTaskIdUseCase.reminderCountState.value)
                    )
                }
            }
        )
    }

    @Test
    fun `when task is RecurringTask, then provided config items match the task`() = runLocalTest {
        runTaskConfigItemsTest(
            taskFactory = RecurringTaskFactory(),
            expected = {
                (it as TaskModel.Task.RecurringTask).let { targetTask ->
                    setOf(
                        BaseItemTaskConfig.Title(targetTask.title),
                        BaseItemTaskConfig.Description(targetTask.description),
                        BaseItemTaskConfig.Priority(targetTask.priority),
                        BaseItemTaskConfig.Frequency(targetTask.frequency),
                        BaseItemTaskConfig.DateConfig.StartDate(targetTask.date.startDate),
                        BaseItemTaskConfig.DateConfig.EndDate(targetTask.date.endDate),
                        BaseItemTaskConfig.Reminders(fakeReadReminderCountByTaskIdUseCase.reminderCountState.value)
                    )
                }
            }
        )
    }

    @Test
    fun `when task is SingleTask, then provided config items match the task`() = runLocalTest {
        runTaskConfigItemsTest(
            taskFactory = SingleTaskFactory(),
            expected = {
                (it as TaskModel.Task.SingleTask).let { targetTask ->
                    setOf(
                        BaseItemTaskConfig.Title(targetTask.title),
                        BaseItemTaskConfig.Description(targetTask.description),
                        BaseItemTaskConfig.Priority(targetTask.priority),
                        BaseItemTaskConfig.DateConfig.Date(targetTask.date.date),
                        BaseItemTaskConfig.Reminders(fakeReadReminderCountByTaskIdUseCase.reminderCountState.value)
                    )
                }
            }
        )
    }

    private fun runTaskConfigItemsTest(
        taskFactory: TaskAbstractFactory,
        expected: (targetTask: TaskModel) -> Set<BaseItemTaskConfig>
    ) = runLocalTest {
        val targetTask = taskFactory.build()
        fakeReadTaskByIdUseCase.taskState.update { targetTask }
        collectUIState()
        advanceUntilIdle()
        viewModel.uiScreenState.value.allTaskConfigItems.let { allTaskConfigItems ->
            assertThat(allTaskConfigItems).containsNoDuplicates()
            expected(targetTask).apply {
                assertThat(allTaskConfigItems).containsExactlyElementsIn(this)
            }
        }
    }

    @Test
    fun `when task is read from use case, task in state matches it`() = runLocalTest {
        assertThat(viewModel.uiScreenState.value.taskModel).isNull()
        fakeReadTaskByIdUseCase.taskState.update { buildDummySingleTask() }
        collectUIState()
        advanceUntilIdle()
        assertThat(viewModel.uiScreenState.value.taskModel).isEqualTo(fakeReadTaskByIdUseCase.taskState.value)
    }

    private fun buildDummySingleTask(): TaskModel.Task.SingleTask {
        return SingleTaskFactory().build()
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun TestScope.collectUIState() {
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }
    }

    private fun initDependencies() {
        taskId = randomUUID()
        fakeReadTaskByIdUseCase = FakeReadTaskByIdUseCase()
        fakeReadReminderCountByTaskIdUseCase = FakeReadReminderCountByTaskIdUseCase()
        testDispatcher = StandardTestDispatcher()
        mockSaveTaskByIdUseCase()
        mockDeleteTaskByIdUseCase()
        mockUpdateTaskTitleByIdUseCase()
        mockUpdateTaskProgressByIdUseCase()
        mockUpdateTaskFrequencyByIdUseCase()
        mockUpdateTaskDateByIdUseCase()
        mockUpdateTaskDescriptionByIdUseCase()
        mockUpdateTaskPriorityByIdUseCase()
        mockVerifyCanSaveTaskUseCase()
    }

    private fun mockSaveTaskByIdUseCase() {
        mockedSaveTaskByIdUseCase = mock {
            onBlocking {
                it.invoke(any())
            }.thenReturn(ResultModel.success(Unit))
        }
    }

    private fun mockDeleteTaskByIdUseCase() {
        mockedDeleteTaskByIdUseCase = mock {
            onBlocking { it.invoke(any()) }.thenReturn(ResultModel.success(Unit))
        }
    }

    private fun mockUpdateTaskTitleByIdUseCase() {
        mockedUpdateTaskTitleByIdUseCase = mock {
            onBlocking { it.invoke(any(), any()) }.thenReturn(ResultModel.success(Unit))
        }
    }

    private fun mockUpdateTaskProgressByIdUseCase() {
        mockedUpdateTaskProgressByIdUseCase = mock {
            onBlocking { this.invoke(any(), any()) }.thenReturn(ResultModel.success(Unit))
        }
    }

    private fun mockUpdateTaskFrequencyByIdUseCase() {
        mockedUpdateTaskFrequencyByIdUseCase = mock {
            onBlocking { it.invoke(any(), any()) }.thenReturn(ResultModel.success(Unit))
        }
    }

    private fun mockUpdateTaskDateByIdUseCase() {
        mockedUpdateTaskDateByIdUseCase = mock {
            onBlocking { it.invoke(any(), any()) }.thenReturn(ResultModel.success(Unit))
        }
    }

    private fun mockUpdateTaskDescriptionByIdUseCase() {
        mockedUpdateTaskDescriptionByIdUseCase = mock {
            onBlocking { it.invoke(any(), any()) }.thenReturn(ResultModel.success(Unit))
        }
    }

    private fun mockUpdateTaskPriorityByIdUseCase() {
        mockedUpdateTaskPriorityByIdUseCase = mock {
            onBlocking { it.invoke(any(), any()) }.thenReturn(ResultModel.success(Unit))
        }
    }

    private fun mockVerifyCanSaveTaskUseCase() {
        mockedVerifyCanSaveTaskUseCase = mock {
            on(it.invoke(any())).thenReturn(false)
        }
    }

    private fun initViewModel() {
        viewModel = CreateTaskViewModel(
            taskId = taskId,
            readTaskByIdUseCase = fakeReadTaskByIdUseCase,
            readReminderCountByTaskIdUseCase = fakeReadReminderCountByTaskIdUseCase,
            saveTaskByIdUseCase = mockedSaveTaskByIdUseCase,
            deleteTaskByIdUseCase = mockedDeleteTaskByIdUseCase,
            updateTaskTitleByIdUseCase = mockedUpdateTaskTitleByIdUseCase,
            updateTaskProgressByIdUseCase = mockedUpdateTaskProgressByIdUseCase,
            updateTaskFrequencyByIdUseCase = mockedUpdateTaskFrequencyByIdUseCase,
            updateTaskDateByIdUseCase = mockedUpdateTaskDateByIdUseCase,
            updateTaskDescriptionByIdUseCase = mockedUpdateTaskDescriptionByIdUseCase,
            updateTaskPriorityByIdUseCase = mockedUpdateTaskPriorityByIdUseCase,
            defaultDispatcher = testDispatcher,
            viewModelScope = TestScope(testDispatcher),
            verifyCanSaveTaskUseCase = mockedVerifyCanSaveTaskUseCase
        )
    }

    private class FakeReadTaskByIdUseCase : ReadTaskByIdUseCase {
        val taskState = MutableStateFlow<TaskModel?>(null)
        override fun invoke(taskId: String): Flow<TaskModel?> {
            return taskState
        }
    }

    private class FakeReadReminderCountByTaskIdUseCase : ReadReminderCountByTaskIdUseCase {
        val reminderCountState = MutableStateFlow(0)
        override fun invoke(taskId: String): Flow<Int> {
            return reminderCountState
        }
    }

}