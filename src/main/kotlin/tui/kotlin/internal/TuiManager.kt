package tui.kotlin

import tui.kotlin.TermManager
import tui.kotlin.core.Layout
import tui.kotlin.core.component.Box
import tui.kotlin.core.component.Column
import tui.kotlin.navigation.Cursor
import tui.kotlin.Color
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
        println(layout.ansiCollection.toString())
    }

    /**
     * write command ansi code to file.
     * to track ansi code result from TuiBuilder
     */
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

    /**
     * set up the terminal to be ready for writing.
     * this method to easier to see the command ansi code escape -
     * in the command file.
     */
    fun writeMode(layout: Layout) {
        layout.ansiCollection.insert(
            0, cursor.hideCursor().plus(cursor.saveCursor())
        )
    }


    fun normalMode(layout: Layout) {
        layout.ansiCollection.append(
            cursor.restoreCursor().plus(cursor.showCursor())
        )
    }

}
