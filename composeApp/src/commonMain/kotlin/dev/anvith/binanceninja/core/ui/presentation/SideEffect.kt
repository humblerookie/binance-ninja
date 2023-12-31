package dev.anvith.binanceninja.core.ui.presentation

sealed interface SideEffect {

    data class Navigate(val route: String) : SideEffect

    data class DisplayToast(val message: String) : SideEffect

    sealed interface MiscEffect : SideEffect

}