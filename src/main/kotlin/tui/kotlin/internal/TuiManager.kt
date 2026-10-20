package tui.kotlin

import tui.kotlin.TermManager
import tui.kotlin.core.Layout
import tui.kotlin.core.component.Column
import tui.kotlin.navigation.Cursor
import java.awt.Color

class TuiManager(

    val cursor: Cursor = Cursor()

) {

    fun write(
        layout: Layout,
        clearScreen: Boolean = true,
        buildCanvas: Boolean = true
    ) {
        if (clearScreen) TermManager().clearScreen()
        writeMode(layout)
        if (layout is Column) {
            layout.buildCanvas()
            layout.appendWidgets()
        }
        normalMode(layout)
        println(layout.layer.stringLayer)
    }


    fun writeMode(layout: Layout) {
        layout.layer.stringLayer.insert(
            0, cursor.hideCursor().plus(cursor.saveCursor())
        )
    }

    fun normalMode(layout: Layout) {
        layout.layer.stringLayer.append(
            cursor.restoreCursor().plus(cursor.showCursor())
        )
    }

}
