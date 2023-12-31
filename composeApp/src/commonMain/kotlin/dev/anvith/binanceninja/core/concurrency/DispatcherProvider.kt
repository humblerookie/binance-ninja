package dev.anvith.binanceninja.core.concurrency

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Inject


interface DispatcherProvider {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}

@Inject
class DefaultDispatcherProvider : DispatcherProvider {

    override fun io() = Dispatchers.IO

    override fun main() = Dispatchers.Main

    override fun default() = Dispatchers.Default

    override fun unconfined() = Dispatchers.Unconfined
}