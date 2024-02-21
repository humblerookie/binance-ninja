package dev.anvith.binanceninja.data.extensions

import app.cash.sqldelight.Transacter


inline fun<reified  T:Transacter> T.transact(crossinline block: T.() -> Unit){
    transaction {
        block()
    }
}