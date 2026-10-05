package tui.kotlin.core.component

import tui.kotlin.RawContent
import tui.kotlin.style.CharStyle
import java.awt.Color

data class Canvas(

    val charCanvas: Char,

    val termSize: Pair<Int, Int>,

    val fgColor: Color,

    val bgColor: Color,

) {

    private val charStyle = CharStyle()

    constructor(termSize: Pair<Int, Int>) : this(
        charCanvas = ' ',
        termSize = termSize,
        fgColor = Color(0,0,0,0),
        bgColor = Color(0,0,0,0)
    )

    fun updateCanvas(
        charCanvas: Char,
        termSize: Pair<Int, Int>,
        fgColor: Color,
        bgColor: Color,
    ): Canvas {
        return this.copy(
            charCanvas = charCanvas,
            termSize = termSize,
            fgColor = fgColor,
            bgColor = bgColor
        )
    }

    fun buildCanvas(): RawContent {
        return RawContent().apply {
            add(charStyle.fgColor(fgColor))
            add(charStyle.bgColor(bgColor))
            add(charCanvas.toString().repeat(
                termSize.first * termSize.second
            ))
            add(charStyle.resetStyle())
        }
    }
}
