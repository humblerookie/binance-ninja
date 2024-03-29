package dev.anvith.binanceninja.features.ui.core

import dev.anvith.binanceninja.core.extensions.toException
import dev.anvith.binanceninja.core.logE
import dev.anvith.binanceninja.di.AppScope
import me.tatarka.inject.annotations.Inject
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusEphemeral
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNUserNotificationCenter

@Inject
@AppScope
actual class PermissionHandler {
  actual fun requestPermission(
    permission: PermissionType,
    onGranted: () -> Unit,
    onDenied: () -> Unit
  ) {
    when (permission) {
      PermissionType.NOTIFICATION -> {
        UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
          UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
        ) { isAuthorized, error ->
          if (error != null)
            logE(
              "Error requesting notification permission",
              error.toException(),
            )
          if (isAuthorized) onGranted() else onDenied()
        }
      }
    }
  }

  actual fun hasPermission(permission: PermissionType, onPermissionResult: (Boolean) -> Unit) {
    when (permission) {
      PermissionType.NOTIFICATION -> checkNotificationStatus(onPermissionResult)
    }
  }

  private fun checkNotificationStatus(onPermissionResult: (Boolean) -> Unit) {
    UNUserNotificationCenter.currentNotificationCenter()
      .getNotificationSettingsWithCompletionHandler { settings ->
        when (settings!!.authorizationStatus) {
          UNAuthorizationStatusAuthorized,
          UNAuthorizationStatusEphemeral,
          UNAuthorizationStatusProvisional -> {
            onPermissionResult(true)
          }
          else -> {
            onPermissionResult(false)
          }
        }
      }
  }
}
