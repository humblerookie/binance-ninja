package dev.anvith.binanceninja.core.res

import cafe.adriel.lyricist.Strings as LyricistStrings
import androidx.compose.ui.text.intl.Locale

object Locales {
    const val EN = "en"
}


fun getLocaleStrings(locale: Locale = Locale.current): Strings {
    return LyricistStrings[Locale.current.toLanguageTag()] ?: EnStrings
}