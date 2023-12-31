package dev.anvith.binanceninja.core.ui.data

import androidx.compose.runtime.Stable

@Stable
data class IList<T>(val list: List<T>):List<T> by list


fun <T> List<T>.lock(): IList<T> = IList(toList())

fun <T> emptyIList(): IList<T> = IList(emptyList())