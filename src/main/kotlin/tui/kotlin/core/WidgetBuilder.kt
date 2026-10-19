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
import tui.kotlin.core.component.Column
import tui.kotlin.core.component.AsciiArt


open class WidgetBuilder(

    val size: Pair<Int, Int>

) {

    // state
    private val widgetCollection = StringBuilder()

    val getWidgets
        get() = widgetCollection

    private var canvas: Canvas = Canvas(size)

    // canvas builder
    fun buildCanvas() {
        val canvas = canvas.buildCanvas()
        widgetCollection.insert(0, canvas.content)
    }

    // canvas setup
    fun canvas(
        charCanvas: Char = ' ',
        fgColor: Color = Color(0,0,0,0),
        bgColor: Color = Color(0,0,0,0)
    ) {
        canvas = canvas.updateCanvas(
            charCanvas = charCanvas,
            dimension = size,
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
            dimension = size
        ).buildBorder(arrangement = arrangement)
        widgetCollection.append(border.content)
    }

    // column layout
    fun column(layout: Column) {
        widgetCollection.append(
            layout.layer.stringLayer
        )
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
        widgetCollection.append(text.content)
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
        widgetCollection.append(asciiArt.content)
    }
}
