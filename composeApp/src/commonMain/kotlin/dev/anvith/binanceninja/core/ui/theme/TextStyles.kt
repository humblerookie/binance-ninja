package dev.anvith.binanceninja.core.ui.theme

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue

object AppText {

    @Composable
    fun H1(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textXXXLarge)
            .weight(FontWeight.Bold)
            .lineHeight(Dimens.textXXXLarge)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun H2(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textXXLarge)
            .weight(FontWeight.Bold)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun H3(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textXLarge)
            .weight(FontWeight.Medium)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun H4(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textSmall)
            .weight(FontWeight.Medium)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun H5(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textSmall)
            .weight(FontWeight.Normal)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun Body1(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textSmall)
            .lineHeight(Dimens.textXXLarge)
            .weight(FontWeight.Medium)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun Body1(
        text: AnnotatedString,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textSmall)
            .lineHeight(Dimens.textXXLarge)
            .weight(FontWeight.Medium)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun Body2(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textSmall)
            .weight(FontWeight.Normal)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun Body2(
        text: AnnotatedString,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textSmall)
            .weight(FontWeight.Normal)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun Caption(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textSmall)
            .weight(FontWeight.Thin)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun Subtitle(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textSmall)
            .weight(FontWeight.Medium)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun Button1(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textSmall)
            .weight(FontWeight.SemiBold)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }

    @Composable
    fun Button2(
        text: String,
        textModifier: TextModifier = TextModifier.default,
        modifier: Modifier = Modifier
    ) {
        TextModifier.size(Dimens.textNormal)
            .weight(FontWeight.Bold)
            .copy(textModifier)
            .applyToText(text = text, modifier = modifier)
    }


    @Composable
    fun InputNormal(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        label: @Composable (() -> Unit)? = null,
        textModifier: TextModifier = TextModifier.default,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        isError: Boolean = false
    ) {
        val style = TextModifier.size(Dimens.textSmall)
            .lineHeight(Dimens.textXXLarge)
            .weight(FontWeight.Medium)
            .copy(textModifier)
            .style()
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            modifier = modifier,
            label = label,
            textStyle = style,
            keyboardActions = keyboardActions,
            isError = isError,

            )
    }

    @Composable
    fun InputNormal(
        value: TextFieldValue,
        onValueChange: (TextFieldValue) -> Unit,
        modifier: Modifier = Modifier,
        label: @Composable (() -> Unit)? = null,
        textModifier: TextModifier = TextModifier.default,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        isError: Boolean = false,
        supportingText: @Composable (() -> Unit)? = null,
    ) {
        val style = TextModifier.size(Dimens.textSmall)
            .lineHeight(Dimens.textXXLarge)
            .weight(FontWeight.Medium)
            .copy(textModifier)
            .style()
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            modifier = modifier,
            label = label,
            textStyle = style,
            keyboardActions = keyboardActions,
            isError = isError,
            supportingText = supportingText
        )
    }

}