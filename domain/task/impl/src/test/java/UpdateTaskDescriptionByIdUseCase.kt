import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.factory.task.SingleTaskFactory
import com.ignatlegostaev.inhabitroutine.data.task.test.FakeTaskRepository
import com.ignatlegostaev.inhabitroutine.domain.model.task.TaskModel
import com.ignatlegostaev.inhabitroutine.domain.task.api.use_case.UpdateTaskDescriptionByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.task.impl.use_case.DefaultUpdateTaskDescriptionByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateTaskDescriptionByIdUseCase {

    private lateinit var useCase: UpdateTaskDescriptionByIdUseCase
    private lateinit var taskRepository: FakeTaskRepository

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when use case is success, then updated task is saved to repository`() = runTest {
        val task = buildRandomTask()
        val newDescription = task.description + "test"
        fillRepository(listOf(task))
        useCase.invoke(taskId = task.id, description = newDescription)
        advanceUntilIdle()
        verifyTaskWithIdHasDescription(taskId = task.id, expectedDescription = newDescription)
    }

    private fun verifyTaskWithIdHasDescription(taskId: String, expectedDescription: String) {
        taskRepository.getTasks().find { it.id == taskId }?.apply {
            assertThat(this.description).isEqualTo(expectedDescription)
        } ?: throw AssertionError()
    }

    private fun fillRepository(tasks: List<TaskModel>) {
        taskRepository.setTasks(tasks)
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
            useCase = DefaultUpdateTaskDescriptionByIdUseCase(
                taskRepository = taskRepository
            )
        }

    }

}