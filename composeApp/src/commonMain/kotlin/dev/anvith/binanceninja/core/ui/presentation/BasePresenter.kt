package dev.anvith.binanceninja.core.ui.presentation

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

abstract class BasePresenter<S, E>(dispatcherProvider: DispatcherProvider) {

  fun dispatchEvent(event: E) = onEvent(event)

  protected abstract fun onEvent(event: E)

  abstract fun initState(): S

  private val _state = MutableStateFlow(this.initState())

  private val mutex = Mutex()

  protected fun updateState(map: (S) -> S) = _state.update(map)

  private val presenterScope: CoroutineScope =
    CoroutineScope(
      SupervisorJob() +
        dispatcherProvider.main() +
        CoroutineExceptionHandler { _, exception ->
          logE("Coroutine threw $exception: \n${exception.stackTraceToString()}")
        }
    )

  val state: StateFlow<S> =
    _state.stateIn(
      presenterScope,
      SharingStarted.WhileSubscribed(5000),
      this.initState(),
    )

  protected fun launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
  ): Job = presenterScope.launch(context, start, block)

  val currentState: S
    get() = _state.value

  val viewEvents
    get() = _events.receiveAsFlow()

  private val _events =
    Channel<SideEffect>(
      capacity = Int.MAX_VALUE,
      onBufferOverflow = BufferOverflow.SUSPEND,
      onUndeliveredElement = { logE("Missed view event $it") },
    )

  protected fun sideEffect(event: SideEffect) {
    launch { _events.send(event) }
  }

  companion object {
    val presenters = mutableMapOf<String, BasePresenter<*, *>>()

    inline fun <reified T> getPresenter(key: String, factory: () -> T): T {
      return (presenters[key] ?: factory()) as T
    }

    fun removeBinding(key: String) {
      presenters.remove(key)?.onCleared()
    }
  }

  fun bind(screen: PresenterScreen) {
    if (!presenters.containsKey(screen.key)) {
      presenters[screen.key] = this
    }
  }

  fun onCleared() {
    presenterScope.cancel()
  }
}
