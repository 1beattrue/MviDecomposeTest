package com.example.mvidecomposetest.presentation

import android.os.Parcelable
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

interface EditContactComponent {

    val model: StateFlow<Model>

    fun onUsernameChanged(username: String)

    fun onPhoneChanged(phone: String)

    fun onSaveChanges()

    @Parcelize
    data class Model(
        val username: String,
        val phone: String
    ) : Parcelable
}