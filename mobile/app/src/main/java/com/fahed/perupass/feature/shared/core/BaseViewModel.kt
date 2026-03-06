package com.fahed.perupass.feature.shared.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(block = block)
    }
}

abstract class BaseSideEffectViewModel<SE> : BaseViewModel() {
    private val _sideEffect = MutableSharedFlow<SE>()
    val sideEffect: SharedFlow<SE> = _sideEffect.asSharedFlow()

    protected fun emitEffect(effect: SE) {
        launch { _sideEffect.emit(effect) }
    }
}
