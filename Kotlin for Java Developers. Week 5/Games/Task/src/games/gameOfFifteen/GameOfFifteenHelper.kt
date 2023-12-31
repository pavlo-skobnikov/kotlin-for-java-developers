package games.gameOfFifteen

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean {
    var transpositions = 0
    val mutablePermutation = permutation.toMutableList()

    for (i in permutation.indices) {
        for (j in 1 until permutation.size) {
            val prev = mutablePermutation[j - 1]
            val next = mutablePermutation[j]

            if (prev > next) {
                transpositions++
                mutablePermutation[j] = prev
                mutablePermutation[j - 1] = next
            }
        }
    }

    return transpositions % 2 == 0
}
