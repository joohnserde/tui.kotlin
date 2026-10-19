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

) : WidgetBuilder(dimension), Layout {

    // state
    override val layer: Layer = Layer()

    fun appendWidgets() {
        layer.stringLayer.append(super.getWidgets)
    }
}
