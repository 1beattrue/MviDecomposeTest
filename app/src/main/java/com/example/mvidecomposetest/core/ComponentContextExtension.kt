package com.example.mvidecomposetest.core

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel


/**
 * Привязка CoroutineScope к жизненному циклу Component по аналогии с viewModelScope
 */
val ComponentContext.componentScope: CoroutineScope
    get() = CoroutineScope(
        Dispatchers.Main.immediate + SupervisorJob()
    ).apply {
        lifecycle.doOnDestroy { cancel() }
    }