package tui.kotlin.core.component

import tui.kotlin.Offset
import tui.kotlin.RawContent
import tui.kotlin.navigation.Cursor
import tui.kotlin.style.CharStyle
import java.awt.Color

class AsciiArt(

    val textString: String,

    val fgColor: Color,

    val bgColor: Color

) {

    private val cursorNav = Cursor()

    private val charStyle = CharStyle()

    private val stringArray = textString.split(Regex("\n"))

    fun buildAsciiArt(
        superimpose: Boolean,
        offset: Offset
    ): RawContent {
        return RawContent().apply {
            if (superimpose) add(superImposeAsciiArt(offset))
        }
    }

    fun insertAsciiArt() {

    }

    fun superImposeAsciiArt(offset: Offset): RawContent {
        return RawContent().apply {
            charStyle.apply {
                add(fgColor(fgColor))
                add(bgColor(bgColor))
            }
            var (row, col) = offset
            for (string in stringArray) {
                add(cursorNav.moveTo(
                        Offset(row, col)
                    )
                )
                add(string)
                row++
            }
            add(charStyle.resetStyle())
        }
    }
}
