package com.ignatlegostaev.inhabitroutine.core.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcherQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcherQualifier