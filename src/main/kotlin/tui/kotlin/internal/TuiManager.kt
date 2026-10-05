package tui.kotlin

import tui.kotlin.TermManager
import tui.kotlin.core.Layout
import tui.kotlin.navigation.Cursor
import java.awt.Color

class TuiManager(

    val cursor: Cursor = Cursor()

) {

    fun write(
        layout: Layout,
        clearScreen: Boolean = true,
    ) {
        writeMode(layout)
        if (clearScreen) TermManager().clearScreen()
        layout.buildCanvas()
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
