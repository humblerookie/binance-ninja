package dev.anvith.binanceninja.core

import io.github.aakira.napier.Napier

fun logD(message: String) = Napier.d(message = message)

fun logE(message: String, throwable: Throwable? = null, tag: String? = null) =
  Napier.e(message, throwable, tag)

fun logE(throwable: Throwable? = null, tag: String? = null, message: () -> String) =
  Napier.e(throwable, tag, message)

fun logI(message: String) = Napier.i(message = message)
