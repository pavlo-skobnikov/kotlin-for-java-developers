package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    this.allDrivers - this.trips.map { it.driver }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    if (minTrips == 0) this.allPassengers
    else this.trips
        .flatMap { it.passengers }
        .groupingBy { it }
        .eachCount()
        .filter { it.value >= minTrips }
        .map { it.key }
        .toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    this.trips
        .filter { it.driver == driver }
        .flatMap { it.passengers }
        .groupingBy { it }
        .eachCount()
        .filter { it.value >= 2 }
        .map { it.key }
        .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> = this.trips
    .flatMap { trip ->
        trip.passengers.map { passenger -> passenger to trip.discount }
    }
    .groupBy({ it.first }, { it.second })
    .filter { (_, discounts) ->
        discounts.size / 2 < discounts.filterNotNull().size
    }
    .map { it.key }
    .toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? =
    this.trips
        .groupBy { it.getDurationRange() }
        .mapValues { it.value.size }
        .maxByOrNull { it.value }
        ?.key


fun Trip.getDurationRange(): IntRange = (duration / 10)
    .mapTo { it * 10 }
    .mapTo { it until it + 10 }

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false

    val driversAndIncomeMap = mutableMapOf<Driver, Double>()
        .withDefault { 0.0 }

    val eightyPercentOfIncome = this.trips
        .onEach {
            driversAndIncomeMap[it.driver] =
                driversAndIncomeMap.getOrElse(it.driver) { 0.0 } + it.cost
        }.fold(0.0) { acc, trip -> acc + trip.cost }
        .mapTo { it * 0.8 }

    val (numOfDriversEarning80PercOfIncome, _) = driversAndIncomeMap.map { it }
        .sortedByDescending { it.value }
        .fold(0 to 0.0) { acc, (_, income) ->
            if (acc.second >= eightyPercentOfIncome) acc
            else acc.first + 1 to acc.second + income
        }

    return numOfDriversEarning80PercOfIncome <= this.allDrivers.size * 0.2
}

fun <T, R> T.mapTo(f: (T) -> R) = f(this)
