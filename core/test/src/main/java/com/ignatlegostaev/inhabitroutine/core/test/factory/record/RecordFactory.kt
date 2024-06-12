package com.ignatlegostaev.inhabitroutine.core.test.factory.record

import com.ignatlegostaev.inhabitroutine.core.test.TestUtil
import com.ignatlegostaev.inhabitroutine.core.util.randomUUID
import com.ignatlegostaev.inhabitroutine.domain.model.record.RecordModel
import com.ignatlegostaev.inhabitroutine.domain.model.record.content.RecordEntry

class RecordFactory {

    fun build(): RecordModel {
        return RecordModel(
            id = randomUUID(),
            taskId = randomUUID(),
            entry = RecordEntry.Done,
            date = TestUtil.buildRandomDate(),
            createdAt = 0L
        )
    }

}