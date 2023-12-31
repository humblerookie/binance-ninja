package dev.anvith.binanceninja.core.ui.data

import androidx.compose.runtime.Immutable

@Immutable
data class IMap<K, V>(private val map: Map<K, V>) : Map<K, V> by map

fun <K, V> Map<K, V>.lock(): IMap<K, V> = IMap(toMap())

fun <K, V> emptyIMap(): IMap<K, V> = IMap(emptyMap())