package com.example.mvidecomposetest.presentation

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.mvidecomposetest.domain.Contact
import kotlinx.parcelize.Parcelize

class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    // класс Value разработан для мультиплатформы
    val stack: Value<ChildStack<Config, ComponentContext>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.ContactList,
            // key = если в приложении несколько стэков навигации
            // persistent = должно ли состояние навигации сохраняться
            handleBackButton = true,
            // childFactory = { configuration, componentContext -> child(configuration, componentContext) }
            childFactory = ::child // method reference
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): ComponentContext {
        return when (config) {
            Config.AddContact -> {
                DefaultAddContactComponent(
                    componentContext = componentContext,
                    onContactSaved = {
                        navigation.pop()
                    }
                )
            }

            Config.ContactList -> {
                DefaultContactListComponent(
                    componentContext = componentContext,
                    onAddContactRequested = {
                        navigation.push(Config.AddContact)
                    },
                    onEditingContactRequested = {
                        navigation.push(Config.EditContact(contact = it))
                    }
                )
            }

            is Config.EditContact -> {
                DefaultEditContactComponent(
                    componentContext = componentContext,
                    contact = config.contact,
                    onContactSaved = {
                        navigation.pop()
                    }
                )
            }
        }
    }

    sealed interface Config : Parcelable { // Navigation Config
        @Parcelize
        object ContactList : Config

        @Parcelize
        object AddContact : Config

        @Parcelize
        data class EditContact(val contact: Contact) : Config
    }
}