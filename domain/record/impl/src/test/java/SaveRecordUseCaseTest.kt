import com.google.common.truth.Truth.assertThat
import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.data.record.test.FakeRecordRepository
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import com.ignatlegostaev.inhabitroutine.domain.record.api.SaveRecordUseCase
import com.ignatlegostaev.inhabitroutine.domain.record.impl.use_case.DefaultSaveRecordUseCase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Before
import org.junit.Test

class SaveRecordUseCaseTest {

    private lateinit var saveRecordUseCase: SaveRecordUseCase
    private lateinit var recordRepository: FakeRecordRepository

    @Before
    fun setUp() {
        init()
    }

    @Test
    fun `when use case is success, then record is saved to repository`() = runTest {
        val result = invokeUseCase()
        (result as? ResultModel.Success)?.let { success ->
            val recordId = success.value
            verifyRepositoryContainsRecordWithId(recordId)
        } ?: throw AssertionError()
    }

    private fun verifyRepositoryContainsRecordWithId(recordId: String) {
        with(getAllRecordsFromRepository()) {
            assertThat(this.find { it.id == recordId }).isNotNull()
        }
    }

    private fun getAllRecordsFromRepository(): List<RecordModel> {
        return recordRepository.getRecords()
    }

    private fun invokeUseCase(
        taskId: String = randomUUID(),
        date: LocalDate = TestUtil.buildRandomDate(),
        requestType: SaveRecordUseCase.RequestType = SaveRecordUseCase.RequestType.EntryDone
    ): ResultModel<String, Throwable> = runBlocking {
        saveRecordUseCase(
            taskId = taskId,
            date = date,
            requestType = requestType
        )
    }

    private fun init() {
        recordRepository = FakeRecordRepository()
        saveRecordUseCase = DefaultSaveRecordUseCase(
            recordRepository = recordRepository
        )
    }

}