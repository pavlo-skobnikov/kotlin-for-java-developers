package nicestring

fun String.isNice(): Boolean {
    var previousChar: Char? = null
    var countOfVowelsEncountered = 0
    var wereDoubleLettersEncountered = false
    var wereBadStringsEncountered = false

    for (char in this) {
        if (char in "aeiou") {
            countOfVowelsEncountered++
        }

        if (char == previousChar) {
            wereDoubleLettersEncountered = true
        }

        if ('b' == previousChar && char in "uae") {
            wereBadStringsEncountered = true
        }

        previousChar = char
    }

    return listOf(
        countOfVowelsEncountered >= 3,
        wereDoubleLettersEncountered,
        !wereBadStringsEncountered,
    ).filter { it }
        .size >= 2
}
