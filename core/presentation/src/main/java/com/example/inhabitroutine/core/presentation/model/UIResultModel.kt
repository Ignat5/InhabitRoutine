package com.example.inhabitroutine.core.presentation.model

sealed interface UIResultModel<out T : Any> {
    val data: T?

    data class Data<T : Any>(override val data: T) : UIResultModel<T>
    data class Loading<T : Any>(override val data: T? = null) : UIResultModel<T>
}

/*
sealed interface UIResultModel<out T : Any> {
    val data: List<T>

    data class Data<T : Any>(override val data: List<T>) : UIResultModel<T>
    data class Loading<T : Any>(override val data: List<T> = emptyList()) : UIResultModel<T>
    data object NoData : UIResultModel<Nothing> {
        override val data: List<Nothing> = emptyList()
    }
}
 */