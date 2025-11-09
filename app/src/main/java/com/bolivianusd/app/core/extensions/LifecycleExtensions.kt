package com.bolivianusd.app.core.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

inline fun <T> LifecycleOwner.collectFlow(
    flow: Flow<T>,
    crossinline action: (T) -> Unit
) = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        flow.collect { value ->
            action(value)
        }
    }
}

fun LifecycleOwner.collectFlows(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

fun <T> Flow<T>.distinctByPrevious(): Flow<T> = flow {
    var previous: T? = null
    collect { value ->
        if (value != previous) {
            previous = value
            emit(value)
        }
    }
}.shareIn(
    scope = CoroutineScope(Dispatchers.Default),
    started = SharingStarted.Lazily,
    replay = 0
)