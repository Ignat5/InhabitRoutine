package com.ignatlegostaev.inhabitroutine.feature.view_habits

import com.ignatlegostaev.inhabitroutine.core.test.factory.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.ViewHabitActionsStateHolder
import com.ignatlegostaev.inhabitroutine.feature.view_habits.config.view_habit_actions.model.ItemHabitAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ViewHabitActionsStateHolderTest {

    private lateinit var stateHolder: ViewHabitActionsStateHolder
    private val habitFactory: HabitYesNoFactory = HabitYesNoFactory()
    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun `when task is archived, then unarchive action is available`() = checkIfContainsActions(
        targetHabit = habitFactory.build().copy(isArchived = true),
        expectedActions = setOf(ItemHabitAction.Unarchive)
    )

    @Test
    fun `when task is unarchived, then archive action is available`() = checkIfContainsActions(
        targetHabit = habitFactory.build().copy(isArchived = false),
        expectedActions = setOf(ItemHabitAction.Archive)
    )

    private fun checkIfContainsActions(
        targetHabit: TaskModel.Habit,
        expectedActions: Set<ItemHabitAction>
    ) = runTest(testDispatcher) {
        initStateHolder(targetHabit)
        collectUIState()
        advanceUntilIdle()
        assertTrue(
            stateHolder.uiScreenState.value.allHabitActionItems.containsAll(expectedActions)
        )
    }

    private fun initStateHolder(targetHabit: TaskModel.Habit) {
        stateHolder = ViewHabitActionsStateHolder(
            taskModel = targetHabit,
            holderScope = TestScope(testDispatcher)
        )
    }

    private fun TestScope.collectUIState() {
        this.backgroundScope.launch {
            stateHolder.uiScreenState.launchIn(this)
        }
    }

}