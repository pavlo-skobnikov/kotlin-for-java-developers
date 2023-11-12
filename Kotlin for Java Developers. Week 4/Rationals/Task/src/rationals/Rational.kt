package rationals

import java.math.BigInteger
import kotlin.NumberFormatException

class Rational(numerator: BigInteger, denominator: BigInteger) :
    Comparable<Rational> {
    private val num: BigInteger
    private val den: BigInteger

    init {
        require(denominator != BigInteger.ZERO) { "Denominator can't be 0" }

        num = if (denominator < BigInteger.ZERO) -numerator else numerator
        den = denominator.abs()
    }

    operator fun plus(addend: Rational): Rational = den.lcd(addend.den)
        .applyFunc { (thisMultiplier, addendMultiplier, leastComMultiple) ->
            Rational(
                (num * thisMultiplier) + (addend.num * addendMultiplier),
                leastComMultiple,
            )
        }

    operator fun unaryMinus(): Rational = Rational(-num, den)
    operator fun minus(subtrahend: Rational): Rational = plus(-subtrahend)

    operator fun times(multiplier: Rational): Rational = Rational(
        num * multiplier.num,
        den * multiplier.den,
    ).simplify()

    operator fun div(divisor: Rational): Rational =
        times(Rational(divisor.den, divisor.num))

    operator fun rangeTo(other: Rational) = RationalRange(this, other)

    override fun hashCode(): Int = simplify()
        .applyFunc { 31 * it.num.hashCode() + it.den.hashCode() }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Rational) return false

        val thisSimplified = simplify()
        val otherSimplified = other.simplify()

        return thisSimplified.num == otherSimplified.num &&
            thisSimplified.den == otherSimplified.den
    }

    override operator fun compareTo(other: Rational): Int {
        val thisSimplified = simplify()
        val otherSimplified = other.simplify()

        if (thisSimplified == otherSimplified) return 0
        if (thisSimplified.den == otherSimplified.den) {
            return thisSimplified.num.compareTo(otherSimplified.num)
        }

        return thisSimplified.den.lcd(otherSimplified.den)
            .applyFunc { (thisMultiplier, otherMultiplier, _) ->
                val thisMultNum = thisSimplified.num * thisMultiplier
                val otherMultNum = otherSimplified.num * otherMultiplier

                thisMultNum.compareTo(otherMultNum)
            }
    }

    override fun toString(): String = simplify()
        .applyFunc {
            if (it.den == BigInteger.ONE) {
                it.num.toString()
            } else {
                "${it.num}/${it.den}"
            }
        }

    private fun simplify(): Rational = num.gcd(den)
        .applyFunc { Rational(num / it, den / it) }

    private fun BigInteger.lcd(other: BigInteger):
        Triple<BigInteger, BigInteger, BigInteger> =
        this.lcm(other)
            .applyFunc { Triple(it / this, it / other, it) }

    private fun BigInteger.lcm(other: BigInteger): BigInteger =
        (this * other) / this.gcd(other)
}

class RationalRange(
    override val start: Rational,
    override val endInclusive: Rational,
) : ClosedRange<Rational>

infix fun BigInteger.divBy(divisor: BigInteger): Rational =
    Rational(this, divisor)

fun BigInteger.toRational(): Rational =
    this divBy BigInteger.ONE

infix fun Long.divBy(divisor: Long): Rational =
    toBigInteger() divBy divisor.toBigInteger()

infix fun Int.divBy(divisor: Int): Rational =
    toBigInteger() divBy divisor.toBigInteger()

fun String.toRational(): Rational = this.split("/")
    .applyFunc { parts ->
        when (parts.size) {
            1 -> parts[0].toBigInteger().toRational()
            2 -> parts[0].toBigInteger() divBy parts[1].toBigInteger()
            else -> throw NumberFormatException("Couldn't parse number into Rational: $this")
        }
    }

private fun <T, R> T.applyFunc(f: (T) -> R) = f(this)

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2,
    )
}
