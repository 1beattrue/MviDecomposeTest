package com.example.mvidecomposetest.presentation

import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.GetContactsUseCase
import com.example.mvidecomposetest.domain.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DefaultContactListComponent(
    val onEditingContactRequested: (Contact) -> Unit,
    val onAddContactRequested: () -> Unit,
) : ContactListComponent {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private val repository: Repository = RepositoryImpl
    private val getContactsUseCase = GetContactsUseCase(repository)

    override val model: StateFlow<ContactListComponent.Model> = getContactsUseCase()
        .map { ContactListComponent.Model(it) }
        .stateIn(
            scope = coroutineScope,
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