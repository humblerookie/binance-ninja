package dev.anvith.binanceninja.core.ui.data

import dev.anvith.binanceninja.core.ui.data.Constants.FORMAT_PRECISION

fun formatPrecision(value: Double, precision: Int = FORMAT_PRECISION): String {
    return value.toString()
        .trimEnd('0')
        .trimEnd('.')
        .run {
            val parts = split(".")
            if (parts.size == 2 && parts[1].length > precision) {
                parts[0] + "." + parts[1].substring(0, precision)
            } else {
                this
            }
        }
}
