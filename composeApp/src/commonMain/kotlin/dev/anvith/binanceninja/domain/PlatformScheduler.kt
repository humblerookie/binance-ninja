package dev.anvith.binanceninja.domain

expect class PlatformScheduler {

    fun schedule()
    fun cancel()
}