package dev.anvith.binanceninja.features.ui.core

import dev.anvith.binanceninja.di.AppScope
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
actual class PermissionHandler {
  actual fun requestPermission(
    permission: PermissionType,
    onGranted: () -> Unit,
    onDenied: () -> Unit
  ) {
    onGranted()
  }

  actual fun hasPermission(permission: PermissionType, onPermissionResult: (Boolean) -> Unit) {
    onPermissionResult(true)
  }
}
