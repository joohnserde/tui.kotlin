package tui.kotlin

class Color(

    val red: Int,

    val green: Int,

    val blue: Int,

    val transparent: Boolean
) {

    constructor(

        red: Int,

        green: Int,

        blue: Int,

    ) : this(red, green, blue, false)

    companion object {
        val BLACK = Color(0, 0, 0)
        val WHITE = Color(255, 255, 255)
    }

}
