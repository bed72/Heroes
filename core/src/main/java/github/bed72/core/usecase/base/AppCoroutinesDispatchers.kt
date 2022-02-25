package github.bed72.core.usecase.base

import kotlinx.coroutines.CoroutineDispatcher

data class AppCoroutinesDispatchers(
    val io: CoroutineDispatcher,
    val main: CoroutineDispatcher,
    val computation: CoroutineDispatcher
)
