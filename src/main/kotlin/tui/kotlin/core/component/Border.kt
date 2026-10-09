package tui.kotlin.core.component

import tui.kotlin.Arrangement
import tui.kotlin.Offset
import tui.kotlin.RawContent
import tui.kotlin.exception.BorderException
import tui.kotlin.navigation.Cursor
import tui.kotlin.style.CharStyle
import java.awt.Color

internal class Border(

    val charBorder: Char,

    val charHorizontal: Char,

    val charVertical: Char,

    val charTopLeft: Char,

    val charTopRight: Char,

    val charBottomLeft: Char,

    val charBottomRight: Char,

    val fgColor: Color,

    val bgColor: Color,

    val dimension: Pair<Int, Int>
) {

    private val cursorNav = Cursor()

    fun buildBorder(arrangement: Arrangement): RawContent {

        val charStyle = CharStyle()

        return RawContent().apply {
            charStyle.apply {
                add(fgColor(fgColor))
                add(bgColor(bgColor))
            }
            when (arrangement) {

                Arrangement.FULL -> {
                    add(buildBorderLine())
                    add(addLeftSide())
                    add(addRightSide())
                }

                Arrangement.VERTICAL -> {
                    add(buildVerticalLine())
                    add(addLeftSide())
                    add(addRightSide())
                }

                Arrangement.HORIZONTAL -> {
                    add(buildHorizontalLine())
                    add(addTopSide())
                    add(addBottomSide())
                }

                Arrangement.RIGHT -> {
                    add(buildRightLine())
                    add(addRightSide())
                }

                Arrangement.LEFT -> {
                    add(buildLeftLine())
                    add(addLeftSide())
                }

                Arrangement.TOP -> {
                    add(buildTopLine())
                    add(addTopSide())
                }

                Arrangement.BOTTOM -> {
                    add(buildBottomLine())
                    add(addBottomSide())
                }

                else -> throw BorderException("apalah coba")
            }
            add(charStyle.resetStyle())
        }
    }


// -----------------------------------------------------------
// border builder method
//
//

// full border line
//
//
    fun buildBorderLine(): RawContent {
        return RawContent().apply {
            add(buildHorizontalLine().content)
            add(buildVerticalLine().content)
        }
    }


// horizontal border line
//
//
    fun buildHorizontalLine(): RawContent {

        val horizontalLine = charHorizontal.toString().repeat(dimension.second)

        return RawContent().apply {
            add(cursorNav.moveTo(Offset(1, 1)))
            add(horizontalLine)
            add(cursorNav.moveTo(Offset(dimension.first, 1)))
            add(horizontalLine)
        }
    }

    fun buildTopLine(): RawContent {

        val horizontalLine = charHorizontal.toString().repeat(dimension.second)

        return RawContent().apply {
            add(cursorNav.moveTo(Offset(1, 1)))
            add(horizontalLine)
        }
    }

    fun buildBottomLine(): RawContent {

        val horizontalLine = charHorizontal.toString().repeat(dimension.second)

        return RawContent().apply {
            add(cursorNav.moveTo(Offset(dimension.first, 1)))
            add(horizontalLine)
        }
    }


// vertical border line
//
//
    fun buildVerticalLine(): RawContent {

        val rawContent = RawContent()

        return try {
            var tmpRowsLoc = dimension.first
            do {
                rawContent.apply {
                    cursorNav.apply {
                        add(moveTo(Offset(tmpRowsLoc, 1)).plus(charVertical))
                        add(moveTo(Offset(tmpRowsLoc, dimension.second)).plus(charVertical))
                    }
                }
                tmpRowsLoc--
            } while (!tmpRowsLoc.equals(0))
            rawContent
        } catch (exception: BorderException) {
            println("")
            throw exception
        }
    }

    fun buildLeftLine(): RawContent {

        val rawContent = RawContent()

        return try {
            var tmpRowsLoc = dimension.first
            do {
                rawContent.add(
                    cursorNav.moveTo(Offset(tmpRowsLoc, 1)).plus(charVertical)
                )
                tmpRowsLoc--
            } while (!tmpRowsLoc.equals(0))
            rawContent
        } catch (exception: BorderException) {
            println("")
            throw exception
        }
    }

    fun buildRightLine(): RawContent {

        val rawContent = RawContent()

        return try {
            var tmpRowsLoc = dimension.first
            do {
                rawContent.add(
                    cursorNav.moveTo(Offset(tmpRowsLoc, dimension.second)).plus(charVertical)
                )
                tmpRowsLoc--
            } while (!tmpRowsLoc.equals(0))
            rawContent
        } catch (exception: BorderException) {
            println("")
            throw exception
        }
    }

// -----------------------------------------------------------
// side char border
//
//

// add left side
//
//
    fun addLeftSide(): RawContent {
        return RawContent().apply {
            add(cursorNav.moveTo(Offset(1, 1)))
            add(charTopLeft)
            add(cursorNav.moveTo(Offset(dimension.first, 1)))
            add(charBottomLeft)
        }
    }


// add right side
//
//
    fun addRightSide(): RawContent {
        return RawContent().apply {
            add(cursorNav.moveTo(Offset(1, dimension.second)))
            add(charTopRight)
            add(cursorNav.moveTo(Offset(dimension.first, dimension.second)))
            add(charBottomRight)
        }
    }


// add top side
//
//
    fun addTopSide(): RawContent {
        return RawContent().apply {
            add(cursorNav.moveTo(Offset(1, 1)))
            add(charTopLeft)
            add(cursorNav.moveTo(Offset(1, dimension.second)))
            add(charTopRight)
        }
    }


// add bottom side
//
//
    fun addBottomSide(): RawContent {
        return RawContent().apply {
            add(cursorNav.moveTo(Offset(dimension.first, 1)))
            add(charBottomLeft)
            add(cursorNav.moveTo(Offset(dimension.first, dimension.second)))
            add(charBottomRight)
        }
    }
}
