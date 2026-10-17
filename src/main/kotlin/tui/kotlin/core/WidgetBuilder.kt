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


open class WidgetBuilder {

    // state
    private val widgetCollection = StringBuilder()

    val getWidgets
        get() = widgetCollection

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
