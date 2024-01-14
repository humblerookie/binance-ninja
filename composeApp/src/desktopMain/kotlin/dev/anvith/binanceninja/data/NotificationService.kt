package dev.anvith.binanceninja.data

import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.models.NotificationModel
import dorkbox.notify.Notify
import dorkbox.notify.Theme
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
actual class NotificationService {
  actual fun notify(items: List<NotificationModel>) {

    items.forEach {
      Notify.create().title(it.title).text(it.message).theme(Theme.defaultDark).showInformation()
    }
  }
}
