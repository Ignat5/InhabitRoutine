import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.reminder.test.FakeReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.DeleteReminderByIdUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultDeleteReminderByIdUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteReminderByIdUseCaseTest {

    private lateinit var useCase: DeleteReminderByIdUseCase
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var reminderManager: ReminderManager

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when call to repo is success, then reset reminder is called`() = runTest {
        val randomReminderId = randomUUID()
        val result = useCase.invoke(randomReminderId)
        assertThat(result).isInstanceOf(ResultModel.Success::class.java)
        advanceUntilIdle()
        verifyResetReminderCalledForReminder(randomReminderId)
    }

    private fun verifyResetReminderCalledForReminder(reminderId: String) {
        verify(reminderManager).resetReminderById(reminderId)
    }

    inner class SetUpRule : TestWatcher() {
        override fun starting(description: Description?) {
            super.starting(description)
            initDependencies()
            initUseCase()
        }

        private fun initDependencies() {
            reminderRepository = FakeReminderRepository()
            reminderManager = mock()
        }

        private fun initUseCase() {
            useCase = DefaultDeleteReminderByIdUseCase(
                reminderRepository = reminderRepository,
                reminderManager = reminderManager
            )
        }

    }

}