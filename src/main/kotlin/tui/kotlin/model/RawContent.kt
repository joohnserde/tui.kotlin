package tui.kotlin

class RawContent(private val _content: StringBuilder) {

    constructor() : this(StringBuilder())

    val content: String
        get() = _content.toString()

    fun add(rawContent: RawContent) {
        _content.append(rawContent.content)
    }

    fun add(stringBuilder: StringBuilder) {
        _content.append(stringBuilder.toString())
    }

    fun add(string: String) {
        _content.append(string)
    }

    fun add(char: Char) {
        _content.append(char)
    }
}
