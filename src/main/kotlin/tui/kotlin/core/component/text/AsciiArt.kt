package tui.kotlin.core.component

import tui.kotlin.Offset
import tui.kotlin.RawContent
import tui.kotlin.navigation.Cursor
import tui.kotlin.style.CharStyle
import java.awt.Color
import java.time.format.TextStyle

class AsciiArt(

    val textString: String,

    val offset: Offset,

    val fgColor: Color,

    val bgColor: Color

) {

    private val cursorNav = Cursor()

    private val charStyle = CharStyle()

    fun buildAsciiArt(): RawContent {
        val stringArray = textString.trimIndent().split(Regex("\n"))
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
