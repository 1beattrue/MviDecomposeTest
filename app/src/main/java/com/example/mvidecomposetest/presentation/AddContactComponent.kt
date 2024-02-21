package com.example.mvidecomposetest.presentation

import kotlinx.coroutines.flow.StateFlow

interface AddContactComponent { // описание экрана(компонента) такое, каким его видит пользователь

    val model: StateFlow<Model>

    fun onUsernameChanged(username: String)

    fun onPhoneChanged(phone: String)

    fun onSaveContactClicked()

    data class Model(
        val username: String,
        val phone: String
    )
}