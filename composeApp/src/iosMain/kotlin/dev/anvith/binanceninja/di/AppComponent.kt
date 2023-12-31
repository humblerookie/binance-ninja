package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.data.cache.FilterRepository
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import platform.UIKit.UIViewController


@Component
@AppScope
abstract class AppComponent(@get:Provides val uiViewControllerProvider: () -> UIViewController) :
    SharedAppComponent() {

    abstract val filterRepository: FilterRepository


}