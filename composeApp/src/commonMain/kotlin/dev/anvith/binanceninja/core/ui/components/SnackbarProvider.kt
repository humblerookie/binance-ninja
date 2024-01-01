package dev.anvith.binanceninja.core.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class SnackbarProvider(
    val showSnack: (
        message: String,
        actionLabel: String?,
        withDismissAction: Boolean,
        handleResult: (SnackbarResult) -> Unit
    ) -> Unit,
)

val LocalSnackbarProvider = compositionLocalOf {
    SnackbarProvider(
        showSnack = { _, _, _, _ -> },
    )
}

fun getSnackbar(scope: CoroutineScope, snackbarHostState: SnackbarHostState): SnackbarProvider {
    return SnackbarProvider(showSnack = { message, actionLabel, withDismissAction, onResult ->
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message,
                actionLabel,
                withDismissAction
            )
            onResult(result)
        }
    })
}