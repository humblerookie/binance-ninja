package dev.anvith.binanceninja.di

import android.content.Context
import dev.anvith.binanceninja.data.cache.FilterRepository
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides


@Component
@AppScope
abstract class AppComponent(
    @get:Provides val context: Context
) : SharedAppComponent() {

    abstract val filterRepository: FilterRepository

}