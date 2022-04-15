package github.bed72.core.usecase.base

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher

interface CoroutinesDispatchers {
    fun io(): CoroutineDispatcher = Dispatchers.IO
    fun main(): CoroutineDispatcher = Dispatchers.Main
    fun default(): CoroutineDispatcher = Dispatchers.Default
    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}

class AppCoroutinesDispatchers @Inject constructor(): CoroutinesDispatchers
