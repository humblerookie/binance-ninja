package dev.anvith.binanceninja.data.cache

import com.russhwolf.settings.Settings
import dev.anvith.binanceninja.core.ui.data.Constants

class UserDataStore(private val settings: Settings) {

    private val defaultCurrencyKey = "default_currency"
    private val notificationIdKey = "notification_id"

    var userCurrency: String
        get() = settings.getString(defaultCurrencyKey, Constants.INR)
        set(value) = settings.putString(defaultCurrencyKey, value)

    var notificationId: Int
        get() = settings.getInt(notificationIdKey, 0)
        set(value) = settings.putInt(notificationIdKey, value)

}