package tui.kotlin.core.component

import tui.kotlin.Offset
import tui.kotlin.RawContent
import tui.kotlin.navigation.Cursor
import tui.kotlin.style.CharStyle
import java.awt.Color

data class Canvas(

    val charCanvas: Char,

    val dimension: Pair<Int, Int>,

    val fgColor: Color,

    val bgColor: Color,

) {

    private val cursorNav = Cursor()

    private val charStyle = CharStyle()

    constructor(dimension: Pair<Int, Int>) : this(
        charCanvas = ' ',
        dimension = dimension,
        fgColor = Color(0,0,0,0),
        bgColor = Color(0,0,0,0)
    )

    fun updateCanvas(
        charCanvas: Char,
        dimension: Pair<Int, Int>,
        fgColor: Color,
        bgColor: Color,
    ): Canvas {
        return this.copy(
            charCanvas = charCanvas,
            dimension = dimension,
            fgColor = fgColor,
            bgColor = bgColor
        )
    }

    fun buildCanvas(): RawContent {
        return RawContent().apply {
            add(charStyle.fgColor(fgColor))
            add(charStyle.bgColor(bgColor))
            /*add(charCanvas.toString().repeat(
                dimension.first * dimension.second
            ))*/

            var height = dimension.first
            while (height != 0) {
                add(cursorNav.moveTo(Offset(height, 0)))
                add(StringBuilder().append(charCanvas).repeat(dimension.second))
                height--
            }
            add(charStyle.resetStyle())
        }
    }
}
