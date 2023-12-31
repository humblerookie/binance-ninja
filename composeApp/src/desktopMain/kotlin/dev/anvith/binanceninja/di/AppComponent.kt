package dev.anvith.binanceninja.di

import dev.anvith.binanceninja.data.cache.FilterRepository
import me.tatarka.inject.annotations.Component


@Component
@AppScope
abstract class AppComponent() : SharedAppComponent() {

    abstract val filterRepository: FilterRepository


}