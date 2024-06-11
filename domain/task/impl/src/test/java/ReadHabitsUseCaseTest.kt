import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.HabitYesNoFactory
import com.ignatlegostaev.inhabitroutine.core.test.factory.SingleTaskFactory
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
    fun `when there are tasks saved in repository, then they are not read`() = runLocalTest {
        val task = buildRandomTask()
        fillRepository(listOf(task))
        useCase().test {
            this.awaitItem().apply {
                assertThat(this).doesNotContain(task)
            }
            this.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when there are habits saved in repository, then they are read`() = runLocalTest {
        val habit = buildRandomHabit()
        fillRepository(listOf(habit))
        useCase().test {
            this.awaitItem().apply {
                assertThat(this).containsExactly(habit)
            }
            this.cancelAndIgnoreRemainingEvents()
        }
    }

    private fun fillRepository(tasks: List<TaskModel>) {
        taskRepository.setTasks(tasks)
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