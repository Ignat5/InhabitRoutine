package com.ignatlegostaev.inhabitroutine.feature.view_reminders

import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ReadRemindersByTaskIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.UpdateReminderUseCase
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.components.ViewRemindersScreenEvent
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.create.components.CreateReminderScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.create_edit_reminder.edit.components.EditReminderScreenResult
import com.ignatlegostaev.inhabitroutine.feature.view_reminders.config.delete_reminder.components.DeleteReminderScreenResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ViewRemindersViewModelTest {

    @get: Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var viewModel: ViewRemindersViewModel
    private lateinit var fakeReadRemindersByTaskIdUseCase: FakeReadRemindersByTaskIdUseCase
    private lateinit var testDispatcher: TestDispatcher

    private val taskId = randomUUID()

    @Mock
    private lateinit var mockedSaveReminderUseCase: SaveReminderUseCase

    @Mock
    private lateinit var mockedUpdateReminderUseCase: UpdateReminderUseCase

    @Mock
    private lateinit var mockedDeleteReminderByIdUseCase: DeleteReminderByIdUseCase

    @Before
    fun setUp() {
        fakeReadRemindersByTaskIdUseCase = FakeReadRemindersByTaskIdUseCase()
        testDispatcher = StandardTestDispatcher()
        viewModel = ViewRemindersViewModel(
            taskId = taskId,
            readRemindersByTaskIdUseCase = fakeReadRemindersByTaskIdUseCase,
            saveReminderUseCase = mockedSaveReminderUseCase,
            updateReminderUseCase = mockedUpdateReminderUseCase,
            deleteReminderByIdUseCase = mockedDeleteReminderByIdUseCase,
            defaultDispatcher = testDispatcher,
            viewModelScope = TestScope(testDispatcher)
        )
    }

    @Test
    fun `when use case emits new reminder data, then state is updated accordingly`() =
        runLocalTest {
            fakeReadRemindersByTaskIdUseCase.allRemindersState.update { getDummyReminders(3) }
            collectUIState()
            advanceUntilIdle()
            viewModel.uiScreenState.value.allRemindersResult.data?.let { data ->
                assertTrue(data.containsAll(fakeReadRemindersByTaskIdUseCase.allRemindersState.value))
            } ?: throw AssertionError()
        }

    @Test
    fun `when create reminder is confirmed, then use case is invoked`() = runLocalTest {
        val confirmResult = with(getSingleDummyReminder()) {
            CreateReminderScreenResult.Confirm(
                reminderTime = this.time,
                reminderType = this.type,
                reminderSchedule = this.schedule
            )
        }
        whenever(
            mockedSaveReminderUseCase.invoke(
                taskId = taskId,
                time = confirmResult.reminderTime,
                type = confirmResult.reminderType,
                schedule = confirmResult.reminderSchedule
            )
        ).thenReturn(ResultModel.success(Unit))
        viewModel.onEvent(
            ViewRemindersScreenEvent.ResultEvent.CreateReminder(confirmResult)
        )
        advanceUntilIdle()
        verify(mockedSaveReminderUseCase).invoke(
            taskId = taskId,
            time = confirmResult.reminderTime,
            type = confirmResult.reminderType,
            schedule = confirmResult.reminderSchedule
        )
    }

    @Test
    fun `when update reminder is confirmed, then use case is invoked`() = runLocalTest {
        val confirmResult = EditReminderScreenResult.Confirm(getSingleDummyReminder())
        whenever(
            mockedUpdateReminderUseCase.invoke(confirmResult.reminderModel)
        ).thenReturn(ResultModel.success(Unit))
        viewModel.onEvent(
            ViewRemindersScreenEvent.ResultEvent.EditReminder(confirmResult)
        )
        advanceUntilIdle()
        verify(mockedUpdateReminderUseCase).invoke(confirmResult.reminderModel)
    }

    @Test
    fun `when delete reminder is confirmed, then use case is invoked`() = runLocalTest {
        val reminderId = getSingleDummyReminder().id
        val confirmResult = DeleteReminderScreenResult.Confirm(
            reminderId = reminderId
        )
        whenever(
            mockedDeleteReminderByIdUseCase.invoke(reminderId)
        ).thenReturn(ResultModel.success(Unit))
        viewModel.onEvent(
            ViewRemindersScreenEvent.ResultEvent.DeleteReminder(confirmResult)
        )
        advanceUntilIdle()
        verify(mockedDeleteReminderByIdUseCase).invoke(confirmResult.reminderId)
    }

    private fun TestScope.collectUIState() {
        this.backgroundScope.launch {
            viewModel.uiScreenState.launchIn(this)
        }
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    private fun getSingleDummyReminder() = getDummyReminders(1).first()

    private fun getDummyReminders(count: Int): List<ReminderModel> {
        val result = mutableListOf<ReminderModel>()
        repeat(count) {
            result.add(
                ReminderModel(
                    id = randomUUID(),
                    taskId = randomUUID(),
                    time = LocalTime(hour = 0, minute = 0),
                    type = ReminderType.NoReminder,
                    schedule = ReminderSchedule.AlwaysEnabled,
                    createdAt = 0L
                )
            )
        }
        return result
    }

    private class FakeReadRemindersByTaskIdUseCase : ReadRemindersByTaskIdUseCase {
        val allRemindersState = MutableStateFlow<List<ReminderModel>>(emptyList())

        override fun invoke(taskId: String): Flow<List<ReminderModel>> {
            return allRemindersState
        }
    }

}