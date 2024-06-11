import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.core.test.factory.HabitNumberFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.model.task.content.TaskProgress
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskProgressByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskProgressByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateTaskProgressByIdUseCaseTest {

    private lateinit var useCase: UpdateTaskProgressByIdUseCase
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when task with provided taskId is not present, then use case results in failure`() =
        runLocalTest {
            val nonExistingTask = buildRandomHabitNumber()
            val newProgress = buildRandomNumberProgress()
            val result = useCase.invoke(taskId = nonExistingTask.id, taskProgress = newProgress)
            advanceUntilIdle()
            assertThat(result).isInstanceOf(ResultModel.Failure::class.java)
        }

    @Test
    fun `when use case is successful, then updated task is saved to repository`() = runLocalTest {
        val task = buildRandomHabitNumber()
        val newProgress = buildRandomNumberProgress()
        fillRepository(listOf(task))
        useCase.invoke(taskId = task.id, taskProgress = newProgress)
        advanceUntilIdle()
        verifyTaskWithIdHasProgress(taskId = task.id, expectedProgress = newProgress)
    }

    private fun verifyTaskWithIdHasProgress(taskId: String, expectedProgress: TaskProgress) {
        (taskRepository.getTasks()
            .find { it.id == taskId } as? TaskModel.Habit.HabitContinuous)?.apply {
            assertThat(this.progress).isEqualTo(expectedProgress)
        } ?: throw AssertionError()
    }

    private fun fillRepository(tasks: List<TaskModel>) {
        taskRepository.setTasks(tasks)
    }

    private fun buildRandomNumberProgress(): TaskProgress.Number {
        return TestUtil.buildRandomTaskNumberProgress()
    }

    private fun buildRandomHabitNumber(): TaskModel.Habit.HabitContinuous {
        return HabitNumberFactory().build()
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
            useCase = DefaultUpdateTaskProgressByIdUseCase(
                taskRepository = taskRepository,
                defaultDispatcher = testDispatcher
            )
        }

    }

}