package dev.anvith.binanceninja.core.ui.presentation

import dev.anvith.binanceninja.core.concurrency.DispatcherProvider
import dev.anvith.binanceninja.core.logE
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.SupervisorJob
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

    private val viewModelScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatcherProvider.main())

    val state: StateFlow<S>
        get() = _state.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            this.initState(),
        )

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context, start, block)
    }

    val currentState: S
        get() = _state.value
    val viewEvents
        get() = _events.receiveAsFlow()

    private val _events = Channel<SideEffect>(
        capacity = Int.MAX_VALUE,
        onBufferOverflow = BufferOverflow.SUSPEND,
        onUndeliveredElement = {
            logE("Missed view event $it")
        },
    )

}