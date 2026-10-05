package tui.kotlin.core.component

import tui.kotlin.navigation.Cursor
import tui.kotlin.style.CharStyle
import java.awt.Color
import tui.kotlin.Offset
import tui.kotlin.TermManager
import tui.kotlin.RawContent

internal class Text(

    val textString: String,

    val offset: Offset,

    val italic: Boolean,

    val bold: Boolean,

    val underLine: Boolean,

    val fgColor: Color,

    val bgColor: Color,

    val strikeThrough: Boolean
) {

    private val cursorNav = Cursor()

    private val charStyle = CharStyle()


    fun buildText(singleLine: Boolean): RawContent {
        return RawContent().apply { 
            if (singleLine) add(buildSingleline())
            else add(buildMultiline())
        }
    }

    fun buildSingleline(): RawContent {
        return RawContent().apply {
            add(cursorNav.moveTo(offset))
            charStyle.apply {
                add(fgColor(fgColor))
                add(bgColor(bgColor))
                if (italic) add(italic())
                if (bold) add(bold())
                if (underLine) add(underLine())
                if (strikeThrough) add(strikeThrough())
            }
            add(textString)
            add(charStyle.resetStyle())
        }
    }

    fun buildMultiline(): RawContent {
        return RawContent() 
    }


}
