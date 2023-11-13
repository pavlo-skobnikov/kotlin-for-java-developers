package games.gameOfFifteen

import board.Cell
import board.Direction
import board.Direction.*
import board.createGameBoard
import games.game.Game
import java.lang.IllegalStateException

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {

    private val board = createGameBoard<Int?>(BOARD_SIZE)

    override fun initialize() {
        SIZE_RANGE
            .flatMap { i -> SIZE_RANGE.map { j -> i to j } }
            .forEachIndexed { idx, (i, j) ->
                board[Cell(i, j)] =
                    initializer.initialPermutation.getOrNull(idx)
            }
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean =
        WINNING_STATE == board.getAllCells().map { get(it.i, it.j) }

    override fun processMove(direction: Direction) {
        with(board) {
            val nullCell = find { it == null } ?: throw IllegalStateException()

            val cellToSwapWith = when (direction) {
                UP -> nullCell.getNeighbour(DOWN)
                DOWN -> nullCell.getNeighbour(UP)
                RIGHT -> nullCell.getNeighbour(LEFT)
                LEFT -> nullCell.getNeighbour(RIGHT)
            } ?: return

            val valueToSwap =
                get(cellToSwapWith) ?: throw IllegalStateException()

            set(cellToSwapWith, null)
            set(nullCell, valueToSwap)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

    companion object {
        private const val BOARD_SIZE = 4
        private val SIZE_RANGE = 1..4
        private val GAME_SQUARES_RANGE = 1..15
        private val WINNING_STATE = GAME_SQUARES_RANGE.toMutableList<Int?>()
            .also { it.add(null) }
    }
}
