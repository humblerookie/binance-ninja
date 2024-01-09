package dev.anvith.binanceninja.domain

expect class PlatformScheduler {

    fun schedule(executor:RequestExecutor)
    fun cancel()
}