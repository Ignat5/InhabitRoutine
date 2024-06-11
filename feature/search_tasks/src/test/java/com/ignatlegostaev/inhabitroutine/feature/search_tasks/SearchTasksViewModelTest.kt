package com.ignatlegostaev.inhabitroutine.feature.search_tasks

import com.ignatlegostaev.inhabitroutine.core.presentation.components.navigation.BaseScreenNavigation
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskDate
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.search_tasks.components.SearchTasksScreenNavigation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class SearchTasksViewModelTest {

    private lateinit var viewModel: SearchTasksViewModel
    private lateinit var readTasksByQueryUseCase: FakeReadTasksByQueryUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        readTasksByQueryUseCase = FakeReadTasksByQueryUseCase()
        viewModel = SearchTasksViewModel(
            readTasksByQueryUseCase = readTasksByQueryUseCase,
            viewModelScope = TestScope(testDispatcher)
        )
    }

    @Test
    fun `when query input changes, then ui state changes accordingly`() = runTest(testDispatcher) {
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }
        val inputQuery = "test"
        viewModel.onEvent(SearchTasksScreenEvent.OnInputQueryUpdate(inputQuery))
        advanceUntilIdle()
        assertTrue(viewModel.uiScreenState.value.searchQuery == inputQuery)
    }

    @Test
    fun `when query is empty, then fetched task collection is empty`() = runTest(testDispatcher) {
        val targetTask = provideTestTask()
        readTasksByQueryUseCase.apply {
            setTasks(listOf(targetTask))
            setFilterQueue { _, task -> task.title == targetTask.title }
        }
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }
        advanceUntilIdle()
        assertTrue(viewModel.uiScreenState.value.allTasks.isEmpty())
        viewModel.onEvent(SearchTasksScreenEvent.OnInputQueryUpdate(targetTask.title))
        advanceUntilIdle()
        viewModel.onEvent(SearchTasksScreenEvent.OnInputQueryUpdate(""))
        advanceUntilIdle()
        assertTrue(viewModel.uiScreenState.value.allTasks.isEmpty())
    }

    @Test
    fun `when query is given, then fetched tasks do match the query`() = runTest(testDispatcher) {
        val matchingTask = provideTestTask().copy(id = "0", title = "abc")
        val notMatchingTask = provideTestTask().copy(id = "1", title = "xyz")
        readTasksByQueryUseCase.apply {
            setTasks(listOf(matchingTask))
            setFilterQueue { _, task -> task.title == matchingTask.title }
        }
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }
        viewModel.onEvent(SearchTasksScreenEvent.OnInputQueryUpdate(matchingTask.title))
        this.advanceUntilIdle()
        assertTrue(viewModel.uiScreenState.value.allTasks.any { it.id == matchingTask.id })
        assertTrue(viewModel.uiScreenState.value.allTasks.none { it.id == notMatchingTask.id })
    }

    @Test
    fun `when leave request received, then navigation state is set accordingly`() =
        runTest(testDispatcher) {
            this.backgroundScope.launch {
                viewModel.uiScreenNavigationState.launchIn(this)
            }
            assertTrue(viewModel.uiScreenNavigationState.value is BaseScreenNavigation.Idle)
            viewModel.onEvent(SearchTasksScreenEvent.OnLeaveRequest)
            advanceUntilIdle()
            viewModel.uiScreenNavigationState.value.let { nextSN ->
                assertTrue(
                    when (nextSN) {
                        is BaseScreenNavigation.Destination -> {
                            nextSN.destination == SearchTasksScreenNavigation.Back
                        }
                        else -> false
                    }
                )
            }
        }

    @Test
    fun `when task is clicked, then navigation state is set accordingly`() =
        runTest(testDispatcher) {
            val clickedTaskId = "test id"
            this.backgroundScope.launch {
                viewModel.uiScreenNavigationState.launchIn(this)
            }
            assertTrue(viewModel.uiScreenNavigationState.value is BaseScreenNavigation.Idle)
            viewModel.onEvent(SearchTasksScreenEvent.OnTaskClick(clickedTaskId))
            advanceUntilIdle()
            viewModel.uiScreenNavigationState.value.let { nextSN ->
                assertTrue(
                    when (nextSN) {
                        is BaseScreenNavigation.Destination -> {
                            when (val dest = nextSN.destination) {
                                is SearchTasksScreenNavigation.EditTask -> {
                                    dest.taskId == clickedTaskId
                                }

                                else -> false
                            }
                        }

                        else -> false
                    }
                )
            }
        }

    private fun provideTestTask() = TaskModel.Task.SingleTask(
        id = "0",
        title = "test task #1",
        description = "",
        date = TaskDate.Day(date = LocalDate(year = 2020, monthNumber = 1, dayOfMonth = 1)),
        priority = 1L,
        isArchived = false,
        versionStartDate = LocalDate(year = 2020, monthNumber = 1, dayOfMonth = 1),
        isDraft = false,
        createdAt = 0L
    )

}