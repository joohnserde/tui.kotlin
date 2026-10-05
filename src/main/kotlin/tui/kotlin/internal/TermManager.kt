package tui.kotlin

import tui.kotlin.exception.TermManagerException
import java.io.File

class TermManager() {

//    Method to get size terminal dimensions
    fun getTerminalDimension(): Pair<Int, Int> {

        /**
         * Run command stty size to get terminal dimensions.
         * but subprocess need access to terminal /dev/tty (current active terminal).
         * so stdin is redirected there before the process start.
         * */
        val process = ProcessBuilder("stty", "size")
            .redirectInput(File("/dev/tty")).start()

        /**
         * Return the input stream connected to the normal output of the subprocess.
         * The returned input stream to be buffered and read it as String
         * */
        val output = process.inputStream.bufferedReader().readText().trim()

        val dimensions = output.split(" ")

        try {

            if (output.isBlank()) {
                throw TermManagerException("output dimensions is blank")
            }

            if (dimensions.size != 2) {
                throw TermManagerException("")
            }
        } catch (e: TermManagerException) {
            println("Failed to get dimensions of terminal")
        }
        return Pair(dimensions[0].toInt(), dimensions[1].toInt())
    }

    fun clearScreen() {

        print("\u001b[H\u001b[2J")

        System.out.flush()
    }

}
