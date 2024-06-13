import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.platform.reminder.api.ReminderManager
import com.ignatlegostaev.inhabitroutine.core.test.factory.reminder.ReminderFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.reminder.test.FakeReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.ResetTaskRemindersUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultResetTaskRemindersUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ResetTaskRemindersUseCaseTest {

    private lateinit var useCase: ResetTaskRemindersUseCase
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var reminderManager: ReminderManager

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when task reminders are present in repo, then reset reminder is called for each reminder`() = runTest {
        val taskId = randomUUID()
        val r1 = buildReminderForTask(taskId)
        val r2 = buildReminderForTask(taskId)
        fillRepository(r1, r2)
        useCase.invoke(taskId)
        verifyResetReminderCalledForEachReminder(r1, r2)
    }

    private fun verifyResetReminderCalledForEachReminder(vararg reminders: ReminderModel) {
        reminders.forEach { reminder ->
            verify(reminderManager).resetReminderById(reminder.id)
        }
    }

    private fun fillRepository(vararg reminders: ReminderModel) {
        reminderRepository.setReminders(reminders.toList())
    }

    private fun buildReminderForTask(taskId: String): ReminderModel {
        return buildReminder().copy(taskId = taskId)
    }

    private fun buildReminder(): ReminderModel {
        return ReminderFactory().build()
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
            useCase = DefaultResetTaskRemindersUseCase(
                reminderRepository = reminderRepository,
                reminderManager = reminderManager
            )
        }

    }

}