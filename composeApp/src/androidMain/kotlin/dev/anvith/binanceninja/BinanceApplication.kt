package dev.anvith.binanceninja

import android.app.Application
import dev.anvith.binanceninja.core.Initializer
import dev.anvith.binanceninja.di.AppComponent
import dev.anvith.binanceninja.di.create

class BinanceApplication : Application() {

    val appComponent by
    lazy(LazyThreadSafetyMode.NONE) { AppComponent::class.create(applicationContext) }

    override fun onCreate() {
        appComponent.initializers.forEach(Initializer::initialize)
        super.onCreate()
    }
}