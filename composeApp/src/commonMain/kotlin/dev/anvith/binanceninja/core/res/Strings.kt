package dev.anvith.binanceninja.core.res

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString

interface Strings {


    val appName: String
    val actionBuy: String
    val actionSell: String
    val labelPrice: String
    val tabCreateFilter: String
    val tabViewFilters: String
    val buySellPrompt: String
    val actionCreateFilter: String
    val labelGreaterThan: String
    val labelLessThan: String
    val selectPrice: String
    val selectAmount: String
    val miscRequirements: String
    val errorCommon: String
    val miscOptions: List<String>
    val labelMerchant: String
    val labelProMerchant: String
    val priceDynamicsLabel: (currency: String, price: Double, labelColor: Color) -> AnnotatedString
    val labelRemove: String
    val actionDynamicsLabel: (currency: String, srcCurrency:String, isBuy: Boolean, amount: Double, labelColor: Color) -> AnnotatedString
    val errorMinMaxEmpty: String
    val errorAmountEmpty: String
    val errorNoFilters: String
    val filterCreationMessage: String
    val errorInvalidInput: String
    val errorSync: String
    val errorNetwork: String
    val selectCurrency: String
    val actionRetry: String
    val permissionDeniedNotifications: String
}
