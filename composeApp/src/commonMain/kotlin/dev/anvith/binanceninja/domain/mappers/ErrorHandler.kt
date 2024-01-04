package dev.anvith.binanceninja.domain.mappers

import dev.anvith.binanceninja.di.AppScope
import dev.anvith.binanceninja.domain.models.ErrorStrings
import io.ktor.client.network.sockets.SocketTimeoutException
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class ErrorHandler {

    fun getMessage(throwable: Throwable):ErrorStrings{
       return when(throwable){
            is SocketTimeoutException-> ErrorStrings.Network
            is Exception-> ErrorStrings.Generic
            else-> ErrorStrings.Generic
        }
    }

}