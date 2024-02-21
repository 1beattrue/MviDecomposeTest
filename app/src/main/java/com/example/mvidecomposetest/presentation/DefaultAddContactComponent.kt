package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.consume
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.AddContactUseCase
import com.example.mvidecomposetest.domain.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultAddContactComponent(
    componentContext: ComponentContext
) : AddContactComponent, ComponentContext by componentContext {

    private val repository: Repository = RepositoryImpl
    private val addContactUseCase = AddContactUseCase(repository)

    init {
        stateKeeper.register(KEY) {
            // функция будет вызвана в момент смены конфигурации или смерти процесса
            model.value // значение будет сохранено
        }
    }

    private val _model = MutableStateFlow(
        stateKeeper.consume(KEY) ?: AddContactComponent.Model(
            username = "",
            phone = ""
        )
    )
    override val model: StateFlow<AddContactComponent.Model>
        get() = _model.asStateFlow()

    override fun onUsernameChanged(username: String) {
        _model.value = model.value.copy(username = username)
    }

    override fun onPhoneChanged(phone: String) {
        _model.value = model.value.copy(phone = phone)
    }

    override fun onSaveContactClicked() {
        val (username, phone) = model.value
        addContactUseCase(username, phone)
    }

    companion object {
        private const val KEY = "DefaultAddContactComponent"
    }
}