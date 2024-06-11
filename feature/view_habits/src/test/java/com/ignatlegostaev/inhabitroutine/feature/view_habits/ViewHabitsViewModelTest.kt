package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.archive_task.components.ArchiveTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.presentation.ui.dialog.delete_task.components.DeleteTaskScreenResult
import com.ignatlegostaev.inhabitroutine.core.test.factory.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ArchiveTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.DeleteTaskByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.SaveTaskDraftUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.filter_habit_by_status.FilterHabitsByStatusUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.filter_habit_by_status.HabitFilterByStatusType
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.components.ViewHabitActionsScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.HabitSort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ViewHabitsViewModelTest {
    private lateinit var viewModel: ViewHabitsViewModel
    private lateinit var readHabitsUseCase: FakeReadHabitsUseCase
    private lateinit var saveTaskDraftUseCase: SaveTaskDraftUseCase
    private lateinit var archiveTaskByIdUseCase: ArchiveTaskByIdUseCase
    private lateinit var deleteTaskByIdUseCase: DeleteTaskByIdUseCase
    private lateinit var filterHabitsByStatusUseCase: FilterHabitsByStatusUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        readHabitsUseCase = FakeReadHabitsUseCase()
        saveTaskDraftUseCase = mock<SaveTaskDraftUseCase>()
        archiveTaskByIdUseCase = mock<ArchiveTaskByIdUseCase>()
        deleteTaskByIdUseCase = mock<DeleteTaskByIdUseCase>()
        filterHabitsByStatusUseCase = mock<FilterHabitsByStatusUseCase>()
        viewModel = ViewHabitsViewModel(
            readHabitsUseCase = readHabitsUseCase,
            saveTaskDraftUseCase = saveTaskDraftUseCase,
            archiveTaskByIdUseCase = archiveTaskByIdUseCase,
            deleteTaskByIdUseCase = deleteTaskByIdUseCase,
            filterHabitsByStatusUseCase = filterHabitsByStatusUseCase,
            defaultDispatcher = testDispatcher,
            viewModelScope = TestScope(testDispatcher)
        )
    }

    @Test
    fun `when archive action is confirmed, then archive use cases is invoked`() =
        runTest(testDispatcher) {
            val taskId = randomUUID()
            whenever(
                archiveTaskByIdUseCase(
                    taskId = taskId,
                    requestType = ArchiveTaskByIdUseCase.RequestType.Archive
                )
            ).thenReturn(ResultModel.success(Unit))
            viewModel.onEvent(
                ViewHabitsScreenEvent.ResultEvent.ArchiveTask(
                    result = ArchiveTaskScreenResult.Confirm(taskId = taskId)
                )
            )
            advanceUntilIdle()
            verify(archiveTaskByIdUseCase).invoke(
                taskId = taskId,
                requestType = ArchiveTaskByIdUseCase.RequestType.Archive
            )
        }

    @Test
    fun `when unarchive action is confirmed, then archive use cases is invoked`() =
        runTest(testDispatcher) {
            val taskId = randomUUID()
            whenever(
                archiveTaskByIdUseCase(
                    taskId = taskId,
                    requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
                )
            ).thenReturn(ResultModel.success(Unit))
            viewModel.onEvent(
                ViewHabitsScreenEvent.ResultEvent.ViewHabitActions(
                    ViewHabitActionsScreenResult.Action.Unarchive(taskId)
                )
            )
            advanceUntilIdle()
            verify(archiveTaskByIdUseCase).invoke(
                taskId = taskId,
                requestType = ArchiveTaskByIdUseCase.RequestType.Unarchive
            )
        }

    @Test
    fun `when delete action is confirmed, then delete use case is invoked`() =
        runTest(testDispatcher) {
            val targetTaskId = randomUUID()
            whenever(
                deleteTaskByIdUseCase(targetTaskId)
            ).thenReturn(ResultModel.success(Unit))
            viewModel.onEvent(
                ViewHabitsScreenEvent.ResultEvent.DeleteTask(
                    result = DeleteTaskScreenResult.Confirm(taskId = targetTaskId)
                )
            )
            advanceUntilIdle()
            verify(deleteTaskByIdUseCase).invoke(targetTaskId)
        }

    @Test
    fun `when filter is not applied, then state matches habits provided by use case`() =
        runTest(testDispatcher) {
            val testTasks = getDummyHabits()
            readHabitsUseCase.allHabitsState.update {
                testTasks
            }
            collectUIScreenState()
            advanceUntilIdle()
            assertTrue(viewModel.uiScreenState.value.filterByStatus == null)
            viewModel.uiScreenState.value.allHabitsResult.data?.let { data ->
                assertTrue(data.containsAll(testTasks))
            } ?: throw AssertionError()
        }

    @Test
    fun `when filter is picked, then state matches the filter`() = runTest(testDispatcher) {
        collectUIScreenState()
        assertTrue(viewModel.uiScreenState.value.filterByStatus == null)
        HabitFilterByStatusType.entries.forEach { filterType ->
            viewModel.onEvent(ViewHabitsScreenEvent.OnPickFilterByStatus(filterType))
            advanceUntilIdle()
            assertTrue(viewModel.uiScreenState.value.filterByStatus == filterType)
        }
    }

    @Test
    fun `when sort is picked, then state matches the sort`() = runTest(testDispatcher) {
        collectUIScreenState()
        HabitSort.entries.forEach { sortType ->
            viewModel.onEvent(ViewHabitsScreenEvent.OnPickSort(sortType))
            advanceUntilIdle()
            assertTrue(viewModel.uiScreenState.value.sort == sortType)
        }
    }

    @Test
    fun `when filter is picked, then it's applied correctly`() = runTest(testDispatcher) {
        val matchFilterHabit = getSingleDummyHabit()
        val notMatchFilterHabit = getSingleDummyHabit()
        readHabitsUseCase.allHabitsState.update {
            listOf(
                matchFilterHabit,
                notMatchFilterHabit
            )
        }
        collectUIScreenState()
        advanceUntilIdle()
        whenever(
            filterHabitsByStatusUseCase.invoke(
                any(), any(), any()
            )
        ).thenReturn(listOf(matchFilterHabit))
        collectUIScreenState()
        val pickedFilter = HabitFilterByStatusType.entries.random()
        viewModel.onEvent(ViewHabitsScreenEvent.OnPickFilterByStatus(pickedFilter))
        advanceUntilIdle()
        viewModel.uiScreenState.value.allHabitsResult.data?.let { data ->
            assertTrue(data.contains(matchFilterHabit))
            assertTrue(!data.contains(notMatchFilterHabit))
        } ?: throw AssertionError()
    }

    private fun getSingleDummyHabit(): TaskModel.Habit = getDummyHabits(1).first()

    private fun getDummyHabits(taskCount: Int = 5): List<TaskModel.Habit> {
        val factory = HabitYesNoFactory()
        val allHabits = mutableListOf<TaskModel.Habit.HabitYesNo>()
        repeat(taskCount) {
            allHabits.add(
                factory.build()
            )
        }
        return allHabits
    }

    private fun getDummyDate(): LocalDate = LocalDate(year = 2000, monthNumber = 1, dayOfMonth = 1)

    private fun TestScope.collectUIScreenState() {
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }
    }


}