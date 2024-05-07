package com.ignatlegostaev.inhabitroutine.core.database.impl.util

import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.ignatlegostaev.inhabitroutine.core.util.ResultModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal fun <T : Any> Query<T>.readQueryList(
    coroutineContext: CoroutineContext
): Flow<List<T>> = this
    .asFlow()
    .mapToList(coroutineContext)

internal fun <T : Any> Query<T>.readOneOrNull(
    coroutineContext: CoroutineContext
): Flow<T?> = this
    .asFlow()
    .mapToOneOrNull(coroutineContext)

internal fun <T : Any> Query<T>.readOne(
    coroutineContext: CoroutineContext
): Flow<T> = this
    .asFlow()
    .mapToOne(coroutineContext)

internal suspend fun <T : Any> runQuery(
    coroutineContext: CoroutineContext,
    query: () -> T
): ResultModel<T, Throwable> = try {
    withContext(coroutineContext) {
        ResultModel.success(query())
    }
} catch (e: Exception) {
    ResultModel.failure(e)
}

internal suspend fun Transacter.runTransaction(
    coroutineContext: CoroutineContext,
    query: () -> Unit
): ResultModel<Unit, Throwable> =
    try {
        withContext(coroutineContext) {
            suspendCoroutine { cnt ->
                this@runTransaction.transaction {
                    this.afterCommit { cnt.resume(Unit) }
                    this.afterRollback { cnt.resumeWithException(RuntimeException()) }
                    query()
                }
            }
            ResultModel.success(Unit)
        }
    } catch (e: Exception) {
        ResultModel.failure(e)
    }