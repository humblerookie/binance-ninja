package dev.anvith.binanceninja.features.ui.core

expect class PermissionHandler {

  fun requestPermission(permission: PermissionType, onGranted: () -> Unit, onDenied: () -> Unit)

  fun hasPermission(permission: PermissionType, onPermissionResult: (Boolean) -> Unit)
}

enum class PermissionType {
  NOTIFICATION
}
