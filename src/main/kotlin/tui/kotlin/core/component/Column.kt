package tui.kotlin.core.component

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

    // param
    val dimension: Pair<Int, Int>,

    // service
    private val termManager: TermManager = TermManager(),

) : Layout {


    // state
    override val layer: Layer = Layer()

    private var canvas: Canvas = Canvas(dimension)


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

    // column layout
    fun column(layout: Column) {
        layer.stringLayer.append(
            layout.layer.stringLayer
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
        bgColor: Color = Color(0,0,0,0)
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


    // text
    fun text(
        textString: String,
        offset: Offset,
        italic: Boolean = false,
        bold: Boolean = false,
        underLine: Boolean = false,
        singleLine: Boolean = false,
        fgColor: Color = Color.WHITE,
        bgColor: Color = Color(0,0,0,0),
        strikeThrough: Boolean = false
    ) {
        val text = Text(
            textString = textString,
            offset = offset,
            italic = italic,
            bold = bold,
            underLine = underLine,
            fgColor = fgColor,
            bgColor = bgColor,
            strikeThrough = strikeThrough
        ).buildText(singleLine = singleLine)
        layer.stringLayer.append(text.content)
    }


    // ascii art
    fun asciiArt(
        textString: String,
        offset: Offset,
        superimpose: Boolean = true,
        fgColor: Color = Color.WHITE,
        bgColor: Color = Color(0,0,0,0)
    ) {
        val asciiArt = AsciiArt(
            textString = textString,
            fgColor = fgColor,
            bgColor = bgColor
        ).buildAsciiArt(superimpose, offset)
        layer.stringLayer.append(asciiArt.content)
    }

}
