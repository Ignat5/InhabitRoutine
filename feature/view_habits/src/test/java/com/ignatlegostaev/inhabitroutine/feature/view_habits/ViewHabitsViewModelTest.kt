package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.core.presentation.model.UIResultModel
import com.ignatlegostaev.inhabitroutine.core.test.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.feature.view_habits.components.ViewHabitsScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.HabitFilterByStatus
import com.ignatlegostaev.inhabitroutine.feature.view_habits.model.HabitSort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewHabitsViewModelTest {

    private lateinit var viewModel: ViewHabitsViewModel
    private lateinit var fakeDataSource: FakeHabitsDataSource
    private lateinit var readHabitsUseCase: FakeReadHabitsUseCase
    private lateinit var saveTaskDraftUseCase: FakeSaveTaskDraftUseCase
    private lateinit var archiveTaskByIdUseCase: FakeArchiveTaskByIdUseCase
    private lateinit var deleteTaskByIdUseCase: FakeDeleteTaskByIdUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        fakeDataSource = FakeHabitsDataSource()
        readHabitsUseCase = FakeReadHabitsUseCase(fakeDataSource)
        saveTaskDraftUseCase = FakeSaveTaskDraftUseCase()
        archiveTaskByIdUseCase = FakeArchiveTaskByIdUseCase(fakeDataSource)
        deleteTaskByIdUseCase = FakeDeleteTaskByIdUseCase(fakeDataSource)
        viewModel = ViewHabitsViewModel(
            readHabitsUseCase = readHabitsUseCase,
            saveTaskDraftUseCase = saveTaskDraftUseCase,
            archiveTaskByIdUseCase = archiveTaskByIdUseCase,
            deleteTaskByIdUseCase = deleteTaskByIdUseCase,
            defaultDispatcher = testDispatcher,
            viewModelScope = TestScope(testDispatcher)
        )
    }

    @Test
    fun `when filter is not applied, then state matches habits provided by use case`() =
        runTest(testDispatcher) {
            fakeDataSource.allHabitsState.update { getDummyHabits() }
            collectUIScreenState()
            advanceUntilIdle()
            assertTrue(viewModel.uiScreenState.value.filterByStatus == null)
            assertTrue(
                when (val result = viewModel.uiScreenState.value.allHabitsResult) {
                    is UIResultModel.Data -> {
                        result.data.containsAll(fakeDataSource.allHabitsState.value)
                    }

                    else -> false
                }
            )
        }

    @Test
    fun `when filter is picked, then state matches the filter`()  = runTest(testDispatcher) {
        collectUIScreenState()
        assertTrue(viewModel.uiScreenState.value.filterByStatus == null)
        HabitFilterByStatus.entries.forEach { filterType ->
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
    fun `when filter OnlyActive is picked, then state contains only tasks matching the filter`() = runTest(testDispatcher) {
//        val activeHabit = getSingleDummyHabit().copy(
//            isArchived = false,
//            date = TaskDate.Period(
//                startDate = Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault()).date,
//                endDate = null
//            )
//        ) as TaskModel.Habit
//
//        val inactiveHabit = getSingleDummyHabit().copy(isArchived = true) as TaskModel.Habit
//        fakeDataSource.allHabitsState.update {
//
//        }
//        collectUIScreenState()
    }

    @Test
    fun `when task is archived, then it doesn't pass OnlyActive filter`() {
        val targetDate = getDummyDate()
        val archivedTask = getSingleDummyHabit().copy(isArchived = true) as TaskModel.Habit
        assertTrue(listOf(archivedTask).filterHabitsByOnlyActive(targetDate).isEmpty())
    }

    @Test
    fun `when task's end date is lesser than today's date, then it doesn't pass OnlyActive filter`() {
        val targetDate = getDummyDate()
        val taskFromThePast = run {
            val startDate = targetDate.minus(1, DateTimeUnit.DAY)
            val endDate = startDate
            getSingleDummyHabit().copy(
                date = TaskDate.Period(
                    startDate = startDate,
                    endDate = endDate
                )
            ) as TaskModel.Habit
        }
        assertTrue(listOf(taskFromThePast).filterHabitsByOnlyActive(targetDate).isEmpty())
    }

    @Test
    fun `when task is archived, then is passes OnlyArchived filter`() {
        val archivedTask = getSingleDummyHabit().copy(isArchived = true) as TaskModel.Habit
        assertTrue(listOf(archivedTask).filterHabitsByOnlyArchived().isNotEmpty())
    }

    @Test
    fun `when task is not archived, then is doesn't pass OnlyArchived filter`() {
        val archivedTask = getSingleDummyHabit().copy(isArchived = false) as TaskModel.Habit
        assertTrue(listOf(archivedTask).filterHabitsByOnlyArchived().isEmpty())
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