package dev.anvith.binanceninja.core.ui.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit


@Composable
private fun getFontFamily(): FontFamily {
    return FontFamily(
        font("montserrat", "montserrat_thin", FontWeight.Light, FontStyle.Normal),
        font("montserrat", "montserrat_regular", FontWeight.Normal, FontStyle.Normal),
        font("montserrat", "montserrat_medium", FontWeight.Medium, FontStyle.Normal),
        font("montserrat", "montserrat_bold", FontWeight.Bold, FontStyle.Normal),
    )
}

interface TextModifier {

    fun font(font: FontFamily): TextModifier

    fun size(size: TextUnit): TextModifier

    fun color(color: Color): TextModifier

    fun lineHeight(height: TextUnit): TextModifier

    fun weight(weight: FontWeight): TextModifier

    fun letterSpacing(spacing: TextUnit): TextModifier

    fun copy(modifier: TextModifier): TextModifier

    @Composable
    fun applyToText(text: String, modifier: Modifier)

    @Composable
    fun applyToText(text: AnnotatedString, modifier: Modifier)

    @Composable
    fun style(): TextStyle

    companion object : TextModifier {
        val default: TextModifier = TextModifierImpl()

        override fun font(font: FontFamily): TextModifier {
            return TextModifierImpl().font(font)
        }

        override fun size(size: TextUnit): TextModifier {
            return TextModifierImpl().size(size)
        }

        override fun color(color: Color): TextModifier {
            return TextModifierImpl().color(color)
        }

        override fun lineHeight(height: TextUnit): TextModifier {
            return TextModifierImpl().lineHeight(height)
        }

        override fun weight(weight: FontWeight): TextModifier {
            return TextModifierImpl().weight(weight)
        }

        override fun letterSpacing(spacing: TextUnit): TextModifier {
            return TextModifierImpl().letterSpacing(spacing)
        }

        override fun copy(modifier: TextModifier): TextModifier {
            return TextModifierImpl(modifier)
        }

        @Composable
        override fun applyToText(
            text: String,
            modifier: Modifier,
        ) {
            //Do Nothing, Let subclasses decide composable implementation
        }

        @Composable
        override fun applyToText(
            text: AnnotatedString,
            modifier: Modifier,
        ) {
            //Do Nothing, Let subclasses decide composable implementation
        }

        @Composable
        override fun style(): TextStyle {
            throw IllegalStateException();
        }

    }
}


open class TextModifierImpl(textModifier: TextModifier? = null) : TextModifier {

    var font: FontFamily? = (textModifier as? TextModifierImpl)?.font
        private set
    var size: TextUnit? = (textModifier as? TextModifierImpl)?.size
        private set
    var color: Color? = (textModifier as? TextModifierImpl)?.color
        private set
    var lineHeight: TextUnit? = (textModifier as? TextModifierImpl)?.lineHeight
        private set
    var weight: FontWeight? = (textModifier as? TextModifierImpl)?.weight
        private set
    var letterSpacing: TextUnit? = (textModifier as? TextModifierImpl)?.letterSpacing
        private set

    override fun font(font: FontFamily): TextModifier {
        this.font = font
        return this
    }

    override fun size(size: TextUnit): TextModifier {
        this.size = size
        return this
    }

    override fun color(color: Color): TextModifier {
        this.color = color
        return this
    }

    override fun lineHeight(height: TextUnit): TextModifier {
        this.lineHeight = height
        return this
    }

    override fun weight(weight: FontWeight): TextModifier {
        this.weight = weight
        return this
    }

    override fun letterSpacing(spacing: TextUnit): TextModifier {
        this.letterSpacing = spacing
        return this
    }

    override fun copy(modifier: TextModifier): TextModifier {
        return if (modifier is TextModifierImpl) {
            font = modifier.font ?: font
            size = modifier.size ?: size
            color = modifier.color ?: color
            lineHeight = modifier.lineHeight ?: lineHeight
            weight = modifier.weight ?: weight
            letterSpacing = modifier.letterSpacing ?: letterSpacing
            TextModifierImpl(this)
        } else {
            modifier
        }
    }

    @Composable
    override fun applyToText(text: String, modifier: Modifier) {
        Text(
            text = text,
            lineHeight = lineHeight ?: TextUnit.Unspecified,
            color = color ?: ThemeColors.onSurface,
            fontSize = size ?: TextUnit.Unspecified,
            fontFamily = font ?: getFontFamily(),
            fontWeight = weight ?: FontWeight.Normal,
            letterSpacing = letterSpacing ?: TextUnit.Unspecified,
            modifier = modifier
        )
    }

    @Composable
    override fun applyToText(text: AnnotatedString, modifier: Modifier) {
        Text(
            text = text,
            lineHeight = lineHeight ?: TextUnit.Unspecified,
            color = color ?: ThemeColors.onSurface,
            fontSize = size ?: TextUnit.Unspecified,
            fontFamily = font ?: getFontFamily(),
            fontWeight = weight ?: FontWeight.Normal,
            letterSpacing = letterSpacing ?: TextUnit.Unspecified,
            modifier = modifier
        )
    }


    @Composable
    override fun style(): TextStyle {
        return TextStyle(
            lineHeight = lineHeight ?: TextUnit.Unspecified,
            color = color ?: TextStyle.Default.color,
            fontSize = size ?: TextUnit.Unspecified,
            fontFamily = font ?: getFontFamily(),
            fontWeight = weight ?: FontWeight.Normal,
            letterSpacing = letterSpacing ?: TextUnit.Unspecified,
        )
    }

    override fun toString(): String {
        return "TextModifierImpl(" +
                "font = $font, " +
                "size= $size, " +
                "weight=$weight, " +
                "lineHeight= $lineHeight," +
                "color= $color, " +
                "spacing= $letterSpacing" +
                ")"
    }

}