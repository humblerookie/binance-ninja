package dev.anvith.binanceninja.domain.models

data class CurrencyModel(
  val code: String,
  val symbol: String,
  val icon: String?,
  val country: String?,
  val isFiat: Boolean,
  val isSelected: Boolean,
  val availableFor: String? = null
)
