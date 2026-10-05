package tui.kotlin.core

import tui.kotlin.Layer

interface Layout {

    val layer: Layer

    fun buildCanvas()
}
