package tui.kotlin.core

import TuiBuilder
import tui.kotlin.Layer

open class Layout(size: Pair<Int, Int>) : TuiBuilder(size) {

    internal val ansiCollection = StringBuilder()

    fun appendTui() {
        ansiCollection.append(getWidgets)
    }

}
