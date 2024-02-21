package com.example.mvidecomposetest.presentation

import kotlinx.coroutines.flow.StateFlow

interface EditContactComponent {

    val model: StateFlow<Model>

    fun onUsernameChanged(username: String)

    fun onPhoneChanged(phone: String)

    fun onSaveChanges()

    data class Model(
        val username: String,
        val phone: String
    )
}