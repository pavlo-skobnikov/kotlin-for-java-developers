@Suppress("MemberVisibilityCanBePrivate")
class WeekTwoAssignments {

    fun isValidIdentifier(s: String): Boolean {
        if (s.isEmpty()) return false

        val firstChar = s[0]

        if (!firstChar.isLetter() && firstChar != '_') return false

        for (c in s.substring(1, s.length)) {
            if (!c.isLetterOrDigit() && c != '_') return false
        }

        return true
    }

    fun List<Int>.sum(): Int {
        var result = 0

        for (i in this) {
            result += i
        }

        return result
    }

    val intList = listOf(1, 2, 3)
    val sum = intList.sum() // Refers to the function declared above
    val charList = listOf('a', 'b', 'c')
    //    charList.sum() // Error: unresolved reference 'sum'
}
