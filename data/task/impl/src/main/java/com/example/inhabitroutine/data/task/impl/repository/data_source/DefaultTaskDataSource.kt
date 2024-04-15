package com.example.inhabitroutine.data.task.impl.repository.data_source

import com.example.inhabitroutine.core.database.api.db.DatabaseDao
import com.example.inhabitroutine.core.database.api.model.derived.TaskWithContentEntity
import com.example.inhabitroutine.core.util.ResultModel
import com.example.inhabitroutine.data.task.impl.repository.model.task.TaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskDataModel
import com.example.inhabitroutine.data.task.impl.repository.util.toTaskEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class DefaultTaskDataSource(
    private val databaseDao: DatabaseDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val json: Json
) : TaskDataSource {

    override fun readTaskById(taskId: String): Flow<TaskDataModel?> =
        databaseDao.readTaskWithContentById(taskId).map { taskWithContent ->
            withContext(ioDispatcher) {
                taskWithContent?.toTaskDataModel(json)
            }
        }

    suspend fun saveTaskWithContent(taskDataModel: TaskDataModel): ResultModel<Unit, Throwable> {
        TODO()
//        TaskWithContentEntity
//        withContext(ioDispatcher) {
//            val taskEntityDef = async {
//                taskDataModel.toTaskEntity(json)
//            }
//        }
//        databaseDao.saveTaskWithContent(
//            taskEntity = taskDataModel.toTaskEntity(json) ?: return ResultModel.failure(
//                IllegalStateException()
//            ),
//            taskContentEntity =,
//            reminderEntity =
//        )
    }
}