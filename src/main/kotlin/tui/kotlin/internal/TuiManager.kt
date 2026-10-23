package tui.kotlin

import tui.kotlin.TermManager
import tui.kotlin.core.Layout
import tui.kotlin.core.component.Box
import tui.kotlin.core.component.Column
import tui.kotlin.navigation.Cursor
import java.awt.Color
import java.io.FileWriter
import java.io.IOException

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
        if (layout is Box) {
            layout.buildCanvas()
            layout.appendTui()
        }
        normalMode(layout)
        println(layout.layer.stringLayer)
    }

    fun write2File(
        rawContent: StringBuilder,
        fileName: String = "RAWCONTENT"
    ) {
        try {
            val file = FileWriter(fileName)
            file.write(rawContent.toString())
            file.close()
            println("Successfully wrote to the file $fileName")
        } catch (err: IOException) {
            println("An error occurred")
            err.printStackTrace()
        }
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
