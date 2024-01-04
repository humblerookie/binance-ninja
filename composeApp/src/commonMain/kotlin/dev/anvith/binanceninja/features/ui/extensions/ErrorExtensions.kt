package dev.anvith.binanceninja.features.ui.extensions

import androidx.compose.runtime.Composable
import cafe.adriel.lyricist.strings
import dev.anvith.binanceninja.domain.models.ErrorStrings
import dev.anvith.binanceninja.domain.models.ErrorStrings.Generic
import dev.anvith.binanceninja.domain.models.ErrorStrings.Network
import dev.anvith.binanceninja.domain.models.ErrorStrings.Sync

@Composable
fun ErrorStrings.localized(): String {
    return when (this) {
        Generic -> strings.errorCommon
        Network -> strings.errorNetwork
        Sync -> strings.errorSync
    }
}