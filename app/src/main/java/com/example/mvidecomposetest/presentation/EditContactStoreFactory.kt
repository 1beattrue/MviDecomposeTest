package com.example.mvidecomposetest.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.mvidecomposetest.data.RepositoryImpl
import com.example.mvidecomposetest.domain.Contact
import com.example.mvidecomposetest.domain.EditContactUseCase
import com.example.mvidecomposetest.domain.Repository

class EditContactStoreFactory {
//    private val store: Store<EditContactStore.Intent, EditContactStore.State, EditContactStore.Label> =
//        storeFactory.create(
//            name = "EditContactStore",
//            initialState = EditContactStore.State(username = "", phone = ""),
//            reducer = ReducerImpl,
//            executorFactory = ::ExecutorImpl
//        ) // подставили непосредственно в делегат

    private val storeFactory: StoreFactory = DefaultStoreFactory()
    private val repository: Repository = RepositoryImpl
    private val editContactUseCase = EditContactUseCase(repository)

    fun create(contact: Contact): EditContactStore = object : EditContactStore,
        Store<EditContactStore.Intent, EditContactStore.State, EditContactStore.Label> by storeFactory.create(
            name = "EditContactStore",
            initialState = EditContactStore.State(
                id = contact.id,
                username = contact.username,
                phone = contact.phone
            ),
            reducer = ReducerImpl,
            executorFactory = ::ExecutorImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeUsername(val username: String) : Msg

        data class ChangePhone(val phone: String) : Msg
    }

    private inner class ExecutorImpl : // нельзя делать object, так как хранит state
        CoroutineExecutor<EditContactStore.Intent, Action, EditContactStore.State, Msg, EditContactStore.Label>() {
        override fun executeIntent(
            intent: EditContactStore.Intent,
            getState: () -> EditContactStore.State
        ) {
            when (intent) {
                is EditContactStore.Intent.ChangePhone -> {
                    dispatch(Msg.ChangePhone(phone = intent.phone))
                }

                is EditContactStore.Intent.ChangeUsername -> {
                    dispatch(Msg.ChangeUsername(username = intent.username))
                }

                EditContactStore.Intent.SaveContact -> {
                    val state = getState()
                    editContactUseCase(
                        contact = Contact(
                            id = state.id,
                            username = state.username,
                            phone = state.phone
                        )
                    )
                    publish(EditContactStore.Label.ContactSaved)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<EditContactStore.State, Msg> {
        override fun EditContactStore.State.reduce(msg: Msg): EditContactStore.State = when (msg) {
            is Msg.ChangePhone -> copy(phone = msg.phone)
            is Msg.ChangeUsername -> copy(username = msg.username)
        }
    }
}