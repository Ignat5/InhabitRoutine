import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.RecurringTaskFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadTasksUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultReadTasksUseCase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class ReadTasksUseCaseTest {

    private lateinit var readTasksUseCase: ReadTasksUseCase
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when exclude drafts, then drafts are not read`() = runLocalTest {
        val notDraftTask = buildRandomSingleTask().copy(isDraft = false)
        val draftTask = buildRandomRecurringTask().copy(isDraft = true)
        fillRepository(notDraftTask, draftTask)
        readTasksUseCase(excludeDrafts = true).test {
            awaitItem().apply {
                assertThat(this).containsExactly(notDraftTask)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `when repository contains habits, then they are not read`() = runLocalTest {
        val habit = buildRandomHabit()
        fillRepository(habit)
        readTasksUseCase(excludeDrafts = false).test {
            awaitItem().apply {
                assertThat(this).doesNotContain(habit)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when repository contains tasks, then they are read`() = runLocalTest {
        val singleTask = buildRandomSingleTask()
        val recurringTask = buildRandomRecurringTask()
        fillRepository(singleTask, recurringTask)
        readTasksUseCase(excludeDrafts = false).test {
            awaitItem().apply {
                assertThat(this).containsExactly(singleTask, recurringTask)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun fillRepository(vararg tasks: TaskModel) {
        taskRepository.setTasks(tasks.toList())
    }

    private fun buildRandomSingleTask(): TaskModel.Task.SingleTask {
        return SingleTaskFactory().build()
    }

    private fun buildRandomRecurringTask(): TaskModel.Task.RecurringTask {
        return RecurringTaskFactory().build()
    }

    private fun buildRandomHabit(): TaskModel.Habit {
        return HabitYesNoFactory().build()
    }

    private fun runLocalTest(block: suspend TestScope.() -> Unit) = runTest(testDispatcher) {
        block()
    }

    inner class SetUpRule : TestWatcher() {
        override fun starting(description: Description?) {
            super.starting(description)
            initDependencies()
            initUseCase()
        }

        private fun initDependencies() {
            taskRepository = FakeTaskRepository()
            testDispatcher = StandardTestDispatcher()
        }

        private fun initUseCase() {
            readTasksUseCase = DefaultReadTasksUseCase(
                taskRepository = taskRepository,
                defaultDispatcher = testDispatcher
            )
        }

    }

}