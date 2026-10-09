import tui.kotlin.Offset
import tui.kotlin.TuiManager
import tui.kotlin.Arrangement
import tui.kotlin.TermManager
import tui.kotlin.core.component.Column
import java.awt.Color

fun main() {


    TuiBuilder()

    val homeScreen = Column(TermManager().getTerminalDimension())
    homeScreen.apply {

        canvas(
            charCanvas = ' ',
            fgColor = Color(0, 0, 156),
            bgColor = Color.BLUE
        )

        canvas(
            charCanvas = ' ',
        )

        border(
            charHorizontal = '%',
            charVertical = '%',
            charTopLeft = '1',
            charTopRight = '2',
            charBottomLeft = '3',
            charBottomRight = '4',
            arrangement = Arrangement.FULL,
            fgColor = Color.ORANGE
        )

        text(
            textString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            offset = Offset(3,4),
            singleLine = true,
            fgColor = Color(255,255,255),
            bgColor = Color(255, 0, 179)
        )

        asciiArt(
            textString = """
    _______  ________  ________  ________
  //      / /    /   \/        \/       /
 //       \/         /         /        \
/         /         /         /         /
\________/\___/____/\___/____/\________/ """.trimIndent(),
            superimpose = true,
            offset = Offset(5, 5),
            fgColor = Color.GREEN,
            bgColor = Color.BLACK

        )


        text(
            textString = " test 121e31r3fqc ",
            offset = Offset(1,4),
            fgColor = Color(255,255,255),
            bgColor = Color(255, 0, 179)
        )

        column(
            Column(dimension = Pair(10,15)).apply {
                canvas(
                    bgColor = Color.BLUE
                )
                border()

                buildCanvas()
            }
        )

        /*text(
            textString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ut neque nunc. Duis sed turpis nec tellus pellentesque cursus.",
            offset = Offset(8, 4),
            bgColor = Color.BLUE
        )*/
    }

    TuiManager().write(homeScreen)
}
