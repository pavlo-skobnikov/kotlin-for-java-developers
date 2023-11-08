
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

internal class WeekTwoAssignmentsKtTest {

    private val tested = WeekTwoAssignments()

    @Test(dataProvider = "validIdentifiers")
    fun `Function isValidIdentifier should return true for valid identifiers`(
        identifier: String,
    ) {
        assert(tested.isValidIdentifier(identifier))
    }

    @Test(dataProvider = "invalidIdentifiers")
    fun `Function isValidIdentifier should return false for invalid identifiers`(
        identifier: String,
    ) {
        assert(!tested.isValidIdentifier(identifier))
    }

    companion object {

        @JvmStatic
        @DataProvider
        fun validIdentifiers(): Array<Array<*>> =
            arrayOf("name", "_name", "_12")
                .map { arrayOf(it) }
                .toTypedArray()

        @JvmStatic
        @DataProvider
        fun invalidIdentifiers(): Array<Array<*>> =
            arrayOf("", "012", "no$")
                .map { arrayOf(it) }
                .toTypedArray()
    }
}
