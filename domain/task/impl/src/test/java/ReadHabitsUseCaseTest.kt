import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.ReadHabitsUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultReadHabitsUseCase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class ReadHabitsUseCaseTest {

    private lateinit var useCase: ReadHabitsUseCase
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when drafts are excluded, then no drafts are read`() = runLocalTest {
        val notDraftHabit = buildRandomHabit().copy(isDraft = false)
        val draftHabit = buildRandomHabit().copy(isDraft = true)
        fillRepository(notDraftHabit, draftHabit)
        useCase.invoke(excludeDrafts = true).test {
            awaitItem().apply {
                assertThat(this).containsExactly(notDraftHabit)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when there are tasks saved in repository, then they are not read`() = runLocalTest {
        val task = buildRandomTask()
        fillRepository(task)
        useCase(excludeDrafts = false).test {
            this.awaitItem().apply {
                assertThat(this).doesNotContain(task)
            }
            this.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when there are habits saved in repository, then they are read`() = runLocalTest {
        val habit = buildRandomHabit()
        fillRepository(habit)
        useCase(excludeDrafts = false).test {
            this.awaitItem().apply {
                assertThat(this).containsExactly(habit)
            }
            this.cancelAndIgnoreRemainingEvents()
        }
    }

    private fun fillRepository(vararg tasks: TaskModel) {
        taskRepository.setTasks(tasks.toList())
    }

    private fun buildRandomHabit(): TaskModel.Habit {
        return HabitYesNoFactory().build()
    }

    private fun buildRandomTask(): TaskModel.Task {
        return SingleTaskFactory().build()
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
            useCase = DefaultReadHabitsUseCase(
                taskRepository = taskRepository,
                defaultDispatcher = testDispatcher
            )
        }

    }

}