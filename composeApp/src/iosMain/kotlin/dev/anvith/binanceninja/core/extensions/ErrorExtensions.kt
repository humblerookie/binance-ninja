package dev.anvith.binanceninja.core.extensions

import platform.Foundation.NSError

fun NSError.toException(): Exception {
    // Create a custom exception message based on NSError details
    val exceptionMessage = "Error Domain: $domain, Code: $code, UserInfo: $userInfo"

    // Creating a Kotlin Exception with the custom message
    return Exception(exceptionMessage)
}