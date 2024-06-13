import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.core.test.factory.reminder.ReminderFactory
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.reminder.test.FakeReminderRepository
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.ReminderModel
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.content.ReminderSchedule
import com.ignatlegostaev.inhabitroutine.domain.model.reminder.type.ReminderType
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SaveReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.api.SetUpNextReminderUseCase
import com.ignatlegostaev.inhabitroutine.domain.reminder.impl.use_case.DefaultSaveReminderUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyBlocking

@OptIn(ExperimentalCoroutinesApi::class)
class SaveReminderUseCaseTest {

    private lateinit var useCase: SaveReminderUseCase
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var setUpNextReminderUseCase: SetUpNextReminderUseCase
    private lateinit var testDispatcher: TestDispatcher

    @Rule
    fun setUpRule() = SetUpRule()

    @Test
    fun `when trying to save reminder that overlaps with already existing ones, then use case results in failure`() =
        runLocalTest {
            val alreadySavedReminder = buildReminder()
            fillRepository(alreadySavedReminder)
            invokeUseCase(
                taskId = alreadySavedReminder.taskId,
                time = alreadySavedReminder.time,
                schedule = alreadySavedReminder.schedule
            ) {
                assertThat(this).isInstanceOf(ResultModel.Failure::class.java)
                with(this as ResultModel.Failure) {
                    assertThat(this.value).isInstanceOf(SaveReminderUseCase.SaveReminderFailure.Overlap::class.java)
                }
            }
        }

    private fun buildReminder(): ReminderModel {
        return ReminderFactory().build()
    }

    private fun fillRepository(vararg reminders: ReminderModel) {
        reminderRepository.setReminders(reminders.toList())
    }

    @Test
    fun `when use case is successful, then set up next reminder is invoked`() = runLocalTest {
        invokeUseCase {
            assertThat(this).isInstanceOf(ResultModel.Success::class.java)
            with(this as ResultModel.Success) {
                val savedReminderId = this.value
                advanceUntilIdle()
                verifySetUpTaskReminderIsInvokedForReminder(savedReminderId)
            }
        }
    }

    private fun verifySetUpTaskReminderIsInvokedForReminder(reminderId: String) {
        verifyBlocking(setUpNextReminderUseCase) {
            invoke(reminderId)
        }
    }

    @Test
    fun `when use case is successful, then reminder is saved to repository`() = runLocalTest {
        invokeUseCase {
            assertThat(this).isInstanceOf(ResultModel.Success::class.java)
            with(this as ResultModel.Success) {
                val savedReminderId = this.value
                verifyRepositoryContainsReminderWithId(savedReminderId)
            }
        }
    }

    private fun verifyRepositoryContainsReminderWithId(reminderId: String) {
        with(getRemindersFromRepository().find { it.id == reminderId }) {
            assertThat(this).isNotNull()
        }
    }

    private suspend fun invokeUseCase(
        taskId: String = randomUUID(),
        time: LocalTime = TestUtil.buildRandomLocalTime(),
        type: ReminderType = ReminderType.entries.random(),
        schedule: ReminderSchedule = ReminderSchedule.AlwaysEnabled,
        block: ResultModel<String, SaveReminderUseCase.SaveReminderFailure>.() -> Unit
    ) {
        with(useCase.invoke(taskId, time, type, schedule)) {
            block(this)
        }
    }

    private fun getRemindersFromRepository(): List<ReminderModel> {
        return reminderRepository.getReminders()
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
            reminderRepository = FakeReminderRepository()
            setUpNextReminderUseCase = mock()
            testDispatcher = StandardTestDispatcher()
        }

        private fun initUseCase() {
            useCase = DefaultSaveReminderUseCase(
                reminderRepository = reminderRepository,
                setUpNextReminderUseCase = setUpNextReminderUseCase,
                externalScope = TestScope(testDispatcher),
                defaultDispatcher = testDispatcher
            )
        }

    }

}