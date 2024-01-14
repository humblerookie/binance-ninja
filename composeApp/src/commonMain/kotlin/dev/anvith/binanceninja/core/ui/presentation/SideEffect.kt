package dev.anvith.binanceninja.core.ui.presentation

sealed interface SideEffect {

  data class Navigate(val route: String) : SideEffect

  data class DisplayMessage(val message: String) : SideEffect

  data class MiscEffect<T>(val data: T) : SideEffect
}
