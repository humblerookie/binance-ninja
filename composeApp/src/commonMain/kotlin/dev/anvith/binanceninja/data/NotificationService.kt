package dev.anvith.binanceninja.data

import dev.anvith.binanceninja.domain.models.NotificationModel

expect class NotificationService {

    fun notify(items: List<NotificationModel>)
}