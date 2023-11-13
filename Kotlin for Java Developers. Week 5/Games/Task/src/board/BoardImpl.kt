package board

import board.Direction.*

class SquareBoardImpl(override val width: Int) : SquareBoard {

    private val cellRows: List<List<Cell>>

    init {
        cellRows = mutableListOf()

        for (i in 1..width) {
            val cellRow = mutableListOf<Cell>()

            for (j in 1..width) {
                cellRow.add(Cell(i, j))
            }

            cellRows.add(cellRow)
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? =
        cellRows.getOrNull(i - 1)
            ?.getOrNull(j - 1)

    override fun getCell(i: Int, j: Int): Cell = getCellOrNull(i, j)
        .let { it ?: throw IllegalArgumentException() }

    override fun getAllCells(): Collection<Cell> = cellRows.flatten()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        jRange.mapNotNull { getCellOrNull(i, it) }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
        iRange.mapNotNull { getCellOrNull(it, j) }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val neighbourI = i.let {
            when (direction) {
                UP -> it - 1
                DOWN -> it + 1
                else -> it
            }
        }
        val neighbourJ = j.let {
            when (direction) {
                LEFT -> it - 1
                RIGHT -> it + 1
                else -> it
            }
        }

        return getCellOrNull(neighbourI, neighbourJ)
    }
}

class GameBoardImpl<T>(
    width: Int,
    squareBoard: SquareBoard = SquareBoardImpl(width),
) :
    SquareBoard by squareBoard,
    GameBoard<T> {

    private val valuesMap: MutableMap<Cell, T?> = mutableMapOf()

    init {
        getAllCells()
            .forEach { set(it, null) }
    }

    override fun get(cell: Cell): T? = valuesMap[cell]

    override fun set(cell: Cell, value: T?) {
        valuesMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        valuesMap.filterValues(predicate)
            .keys

    override fun find(predicate: (T?) -> Boolean): Cell? = filter(predicate)
        .firstOrNull()

    override fun any(predicate: (T?) -> Boolean): Boolean = valuesMap.values
        .any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean = valuesMap.values
        .all(predicate)
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)
