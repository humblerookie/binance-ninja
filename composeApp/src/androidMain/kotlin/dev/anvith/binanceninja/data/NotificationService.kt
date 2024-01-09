package dev.anvith.binanceninja.data

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dev.anvith.binanceninja.MainActivity
import dev.anvith.binanceninja.R
import dev.anvith.binanceninja.data.cache.UserDataStore
import dev.anvith.binanceninja.domain.models.NotificationModel
import dev.anvith.binanceninja.features.ui.core.PermissionHandler
import dev.anvith.binanceninja.features.ui.core.PermissionType
import java.util.UUID
import me.tatarka.inject.annotations.Inject

@Inject
actual class NotificationService(
    private val context: Context,
    private val userDataStore: UserDataStore,
    private val permissionHandler: PermissionHandler
) {
    private val channelId = "MATCH_ORDERS"
    @SuppressLint("MissingPermission")
    actual fun notify(items: List<NotificationModel>) {
        with(NotificationManagerCompat.from(context)) {
            permissionHandler.hasPermission(PermissionType.NOTIFICATION) { hasPermission ->
                if (hasPermission) {
                    createNotificationChannel()
                    createNotification(items).forEach {
                        val id = userDataStore.notificationId + 1
                        userDataStore.notificationId = id
                        notify(id, it)
                    }
                }
            }
        }
    }

    private fun createNotification(items: List<NotificationModel>): List<Notification> {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val uuid = UUID.randomUUID().toString()
        return buildList {
            items.forEach {
                add(
                    NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(it.title)
                        .setContentText(it.message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setGroup(uuid)
                        .build()
                )
            }
        }
    }

    private fun createNotificationChannel() {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && notificationManager.getNotificationChannel(channelId) == null
        ) {
            val name = context.getString(R.string.match_notification_title)
            val descriptionText = context.getString(R.string.match_notification_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            notificationManager.createNotificationChannel(channel)
        }
    }


}