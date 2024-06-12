import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskTitleByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskTitleByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateTaskTitleByIdUseCaseTest {

    private lateinit var useCase: UpdateTaskTitleByIdUseCase
    private lateinit var taskRepository: FakeTaskRepository

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when task with provided taskId is not present, then use case result is failure`() =
        runTest {
            val nonExistingTask = buildRandomTask()
            val newTitle = nonExistingTask.title + "test"
            val result = useCase.invoke(taskId = nonExistingTask.id, title = newTitle)
            advanceUntilIdle()
            assertThat(result).isInstanceOf(ResultModel.Failure::class.java)
        }

    @Test
    fun `when use case is successful, then updated task is saved to repository`() = runTest {
        val task = buildRandomTask()
        val newTitle = task.title + "test"
        fillRepository(task)
        useCase.invoke(taskId = task.id, title = newTitle)
        advanceUntilIdle()
        verifyTaskWithIdHasTitle(taskId = task.id, expectedTitle = newTitle)
    }

    private fun verifyTaskWithIdHasTitle(taskId: String, expectedTitle: String) {
        taskRepository.getTasks().find { it.id == taskId }?.apply {
            assertThat(this.title).isEqualTo(expectedTitle)
        } ?: throw AssertionError()
    }

    private fun fillRepository(vararg tasks: TaskModel) {
        taskRepository.setTasks(tasks.toList())
    }

    private fun buildRandomTask(): TaskModel {
        return SingleTaskFactory().build()
    }

    inner class SetUpRule : TestWatcher() {
        override fun starting(description: Description?) {
            super.starting(description)
            initDependencies()
            initUseCase()
        }

        private fun initDependencies() {
            taskRepository = FakeTaskRepository()
        }

        private fun initUseCase() {
            useCase = DefaultUpdateTaskTitleByIdUseCase(
                taskRepository = taskRepository
            )
        }
    }

}