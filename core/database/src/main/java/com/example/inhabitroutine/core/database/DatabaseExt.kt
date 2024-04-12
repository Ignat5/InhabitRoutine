package com.example.inhabitroutine.core.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

fun <T : Any> Query<T>.readQueryList(
    coroutineContext: CoroutineContext
): Flow<List<T>> = this
    .asFlow()
    .mapToList(coroutineContext)