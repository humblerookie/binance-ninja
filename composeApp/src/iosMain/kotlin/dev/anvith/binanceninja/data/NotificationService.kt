package dev.anvith.binanceninja.data

import dev.anvith.binanceninja.core.logD
import dev.anvith.binanceninja.data.cache.UserDataStore
import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.models.NotificationModel
import dev.anvith.binanceninja.features.ui.core.PermissionHandler
import dev.anvith.binanceninja.features.ui.core.PermissionType
import me.tatarka.inject.annotations.Inject
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter

@Inject
@AppScope
actual class NotificationService(
    private val userDataStore: UserDataStore,
    private val permissionHandler: PermissionHandler,
) {
    actual fun notify(items: List<NotificationModel>) {
        permissionHandler.hasPermission(PermissionType.NOTIFICATION) { hasPermission ->
            if (hasPermission) {
                items.forEach {
                    val content = UNMutableNotificationContent().apply {
                        setTitle(it.title)
                        setBody(it.message)
                        setSound(UNNotificationSound.defaultSound())
                    }
                    val trigger =
                        UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                            1.0,
                            repeats = false
                        )
                    val identifier = userDataStore.notificationId + 1
                    userDataStore.notificationId = identifier
                    val request = UNNotificationRequest.requestWithIdentifier(
                        identifier.toString(),
                        content = content,
                        trigger = trigger
                    )
                    val center = UNUserNotificationCenter.currentNotificationCenter()

                    center.addNotificationRequest(request) { error ->
                        logD("IOS Notification Error: $error")
                    }
                }
            }
        }
    }

}