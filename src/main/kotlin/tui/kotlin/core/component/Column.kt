package tui.kotlin.core.component

import WidgetBuilder
import com.sun.tools.javac.tree.TreeInfo
import tui.kotlin.Arrangement
import tui.kotlin.TermManager
import tui.kotlin.Layer
import tui.kotlin.core.component.Text
import tui.kotlin.core.component.Canvas
import tui.kotlin.core.component.Border
import java.awt.Color
import tui.kotlin.Offset
import tui.kotlin.core.Layout
import tui.kotlin.core.component.AsciiArt

class Column(

    val dimension: Pair<Int, Int>

) : WidgetBuilder(), Layout {

    // state
    override val layer: Layer = Layer()

    private var canvas: Canvas = Canvas(dimension)


    fun appendWidgets() {
        layer.stringLayer.append(getWidgets)
    }

    // canvas builder
    override fun buildCanvas() {
        val canvas = canvas.buildCanvas()
        layer.stringLayer.insert(0, canvas.content)
    }

    // canvas setup
    fun canvas(
        charCanvas: Char = ' ',
        fgColor: Color = Color(0,0,0,0),
        bgColor: Color = Color(0,0,0,0)
    ) {
        canvas = canvas.updateCanvas(
            charCanvas = charCanvas,
            dimension = dimension,
            fgColor = fgColor,
            bgColor = bgColor
        )
    }

    // border
    fun border(
        charBorder: Char = '#',
        charHorizontal: Char = '#',
        charVertical: Char = '#',
        charTopLeft: Char = '#',
        charTopRight: Char = '#',
        charBottomLeft: Char = '#',
        charBottomRight: Char = '#',
        arrangement: Arrangement = Arrangement.FULL,
        fgColor: Color = Color.WHITE,
        bgColor: Color = Color(0,0,0,0),
    ) {
        val border = Border(
            charBorder = charBorder,
            charHorizontal = charHorizontal,
            charVertical = charVertical,
            charTopLeft = charTopLeft,
            charTopRight = charTopRight,
            charBottomLeft = charBottomLeft,
            charBottomRight = charBottomRight,
            fgColor = fgColor,
            bgColor = bgColor,
            dimension = dimension
        ).buildBorder(arrangement = arrangement)
        layer.stringLayer.append(border.content)
    }
}
