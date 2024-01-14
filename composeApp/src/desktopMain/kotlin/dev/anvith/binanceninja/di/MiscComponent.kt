package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.data.TasksComponent
import kotlinx.coroutines.Dispatchers
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Provides

actual interface MiscComponent : TasksComponent {

  @Provides @AppScope fun DesktopDispatcherProvider.bind(): DispatcherProvider = this
}

@Inject
class DesktopDispatcherProvider : DispatcherProvider {

  override fun io() = Dispatchers.IO

  override fun main() = Dispatchers.Default

  override fun default() = Dispatchers.Default

  override fun unconfined() = Dispatchers.Unconfined
}
