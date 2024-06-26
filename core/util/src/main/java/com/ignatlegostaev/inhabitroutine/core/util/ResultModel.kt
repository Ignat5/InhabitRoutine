package com.ignatlegostaev.inhabitroutine.core.util

sealed class ResultModel<out T, out E> {
    data class Success<out T>(val value: T) : ResultModel<T, Nothing>()
    data class Failure<out E>(val value: E) : ResultModel<Nothing, E>()

    companion object {
        fun <T> success(value: T): ResultModel<T, Nothing> = ResultModel.Success(value)
        fun <E> failure(value: E): ResultModel<Nothing, E> = ResultModel.Failure(value)
    }
}

inline fun <T, E, R> ResultModel<T, E>.mapSuccess(transform: (T) -> R): ResultModel<R, E> =
    when (this) {
        is ResultModel.Success -> ResultModel.Success<R>(transform(this.value))
        is ResultModel.Failure -> ResultModel.Failure<E>(this.value)
    }

inline fun <T, E, R> ResultModel<T, E>.mapFailure(transform: (E) -> R): ResultModel<T, R> =
    when (this) {
        is ResultModel.Success -> ResultModel.Success<T>(this.value)
        is ResultModel.Failure -> ResultModel.Failure<R>(transform(this.value))
    }