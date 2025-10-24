package com.leecoder.stockchart.ui.base

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


abstract class StateViewModel<State, SideEffect>(
    initialState: State
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + CoroutineExceptionHandler() { _, throwable ->
            Log.e("Leecoder", "[Exception] -> ${throwable.message}")
        }

    private val reduceChannel = Channel<State.() -> State>()

    protected suspend fun reduceState(reduce: State.() -> State) {
        reduceChannel.send(reduce)
    }

    val state: StateFlow<State> = reduceChannel.receiveAsFlow()
        .runningFold(initialState, ::reduce)
        .onEach {  }
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialState)

    private fun reduce(currentState: State, reduce: State.() -> State) = currentState.reduce()

    private val _sideEffectChannel = Channel<SideEffect>(
        capacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private val sideEffectFlow = _sideEffectChannel.receiveAsFlow()

    protected suspend fun sendSideEffect(sideEffect: SideEffect) {
        _sideEffectChannel.send(sideEffect)
    }

    @Composable
    fun collectSideEffect(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        consumeSideEffect: (suspend (sideEffect: SideEffect) -> Unit)
    ) {
        val sideEffectFlow = sideEffectFlow
        val lifecycleOwner = LocalLifecycleOwner.current

        val sideEffectConsumer by rememberUpdatedState(consumeSideEffect)

        LaunchedEffect(sideEffectFlow, lifecycleOwner) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
                sideEffectFlow.collect {
                    launch {
                        sideEffectConsumer(it)
                    }
                }
            }
        }
    }
}