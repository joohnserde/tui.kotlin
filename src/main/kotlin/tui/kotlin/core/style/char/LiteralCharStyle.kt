package tui.kotlin.style

import tui.kotlin.Color
import tui.kotlin.AnsiEscape

internal object LiteralCharStyle {

    const val RESET_STYLE = "${AnsiEscape.ESC}0m"

    const val BOLD = "${AnsiEscape.ESC}1m"

    const val ITALIC = "${AnsiEscape.ESC}3m"

    const val UNDERLINE = "${AnsiEscape.ESC}4m"

    const val STRIKETHROUGH = "${AnsiEscape.ESC}9m"

    fun fgColor(color: Color): String {
        return "${AnsiEscape.ESC}38;2;${color.red};${color.green};${color.blue}m"
    }

    fun bgColor(color: Color): String {
        return "${AnsiEscape.ESC}48;2;${color.red};${color.green};${color.blue}m"
    }

}
