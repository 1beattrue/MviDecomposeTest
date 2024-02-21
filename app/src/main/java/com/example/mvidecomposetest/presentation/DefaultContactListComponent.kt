package com.example.mvidecomposetest.presentation

import com.arkivanov.decompose.ComponentContext
import com.example.mvidecomposetest.core.componentScope
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.GetContactsUseCase
import com.example.mvidecomposetest.domain.Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DefaultContactListComponent(
    componentContext: ComponentContext,
    val onEditingContactRequested: (Contact) -> Unit,
    val onAddContactRequested: () -> Unit,
) : ContactListComponent, ComponentContext by componentContext { // реализация всех методов при помощи делегата

    private val repository: Repository = RepositoryImpl
    private val getContactsUseCase = GetContactsUseCase(repository)

    override val model: StateFlow<ContactListComponent.Model> = getContactsUseCase()
        .map { ContactListComponent.Model(it) }
        .stateIn(
            scope = componentScope,
            started = SharingStarted.Lazily,
            initialValue = ContactListComponent.Model(listOf())
        )

    override fun onContactClicked(contact: Contact) {
        onEditingContactRequested(contact)
    }

    override fun onAddContactClicked() {
        onAddContactRequested()
    }
}