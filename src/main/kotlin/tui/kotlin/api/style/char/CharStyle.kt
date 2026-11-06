package tui.kotlin.style

import tui.kotlin.Color
import tui.kotlin.AnsiEscape

class CharStyle {

    fun resetStyle() = LiteralCharStyle.RESET_STYLE

    fun bold() = LiteralCharStyle.BOLD

    fun italic() = LiteralCharStyle.ITALIC

    fun underLine() = LiteralCharStyle.UNDERLINE

    fun strikeThrough() = LiteralCharStyle.STRIKETHROUGH

    fun fgColor(color: Color) = LiteralCharStyle.fgColor(color)

    fun bgColor(color: Color) = LiteralCharStyle.bgColor(color)
}
