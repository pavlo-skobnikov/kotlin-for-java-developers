package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

private typealias PairOfSecretAndGuessMismatchCount = Pair<Int, Int>

fun evaluateGuess(secret: String, guess: String): Evaluation {
    if (secret.length != guess.length) {
        throw IllegalArgumentException(
            "`secret` and `guess` must be of the same length",
        )
    }

    var rightPosition = 0
    val notEncounteredSecretChars = mutableMapOf<Char, Int>()
    val notEncounteredGuessChars = mutableMapOf<Char, Int>()

    guess.forEachIndexed { idx, guessChar ->
        val secretChar = secret[idx]

        if (guessChar == secretChar) {
            rightPosition++
        } else {
            notEncounteredSecretChars[secretChar] =
                notEncounteredSecretChars.getOrDefault(secretChar, 0) + 1
            notEncounteredGuessChars[guessChar] =
                notEncounteredGuessChars.getOrDefault(guessChar, 0) + 1
        }
    }

    val wrongPosition = notEncounteredSecretChars
        .map { (secretChar, secretCharCount) ->
            val guessCharCount = notEncounteredGuessChars.getOrDefault(
                secretChar,
                0,
            )
            minOf(secretCharCount, guessCharCount)
        }
        .sum()

    return Evaluation(rightPosition, wrongPosition)
}
