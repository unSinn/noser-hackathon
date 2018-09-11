package com.noser.hackathon

import com.noser.hackathon.server.Board
import com.noser.hackathon.server.Color
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.min


fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) + start

@Service
@EnableScheduling
class AiService {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun calculatePlay(board: Board, color: Color): Int {
        log.info("Calculating for Board ${board.boardId} ...")


        var index = calculate(color.toString(), board)

        if (index < 0) {
            return getRandomAvailableColumn(board)
        }

        return index
    }

    fun getRandomAvailableColumn(board: Board): Int {
        val availableColumns = mutableListOf<Int>()

        board.grid.forEachIndexed { index, column ->
            if (column[Config.BOARD_HEIGHT - 1].isBlank()) {
                availableColumns.add(index)
            }
        }

        var column = 0

        if (availableColumns.size <= 0) {
            log.info("No more space available! All columns are full")
        } else {
            val random = (0 until availableColumns.size).random()
            column = availableColumns[random]
            log.info("Calculated random Play: Column $column for Board ${board.boardId}")
        }

        return column
    }

    fun calculate(color: String, board: Board): Int {
        var enemyColor = Color.O.toString()
        if(color.equals( Color.O.toString() )) {
            enemyColor = Color.X.toString()
        }
        var rowScores = findInRow(color, board)
        var enemyRowScores = findInRow(enemyColor, board)
        var columnScores = findAmountInColumn(color, board)
        var enemyColumnScores = findAmountInColumn(enemyColor, board)

        // find winning columns
        for(score in 3 downTo 1) {
            if (rowScores.max() == score) {
                return rowScores.indexOf(score)
            }
            if (columnScores.max() == score) {
                return columnScores.indexOf(score)
            }

            // find enemy winning columns
            if (enemyRowScores.max() == score) {
                return enemyRowScores.indexOf(score)
            }
            if (enemyColumnScores.max() == score) {
                return enemyColumnScores.indexOf(score)
            }
        }

        return -1

    }

    fun findAmountInColumn(color: String, board: Board): Array<Int> {
        var count = 0
        var scoreList = Array(Config.BOARD_WITH, { i -> 0 })

        board.grid.forEachIndexed { columnIndex, column ->
            count = 0

            column.forEachIndexed { rowIndex, currentColor ->
                if (currentColor == color) {
                    // count chosen color
                    count++
                } else if (currentColor.isBlank()) {
                    // no mor coins
                    if (rowIndex < Config.BOARD_HEIGHT - 1) {
                        // we found column with the given amount and there is space left => return the column index
                        scoreList[columnIndex] = count
                    } else {
                        // go to next column
                        count = 0
                    }
                } else {
                    // other color
                    count = 0
                }
            }
        }

        return scoreList
    }

    fun findInRow(color: String, board: Board): Array<Int> {
        var count = 0
        var scoreList = Array(Config.BOARD_WITH, { i -> 0 })
        var rowScoreList = Array(Config.BOARD_HEIGHT, { i -> 0 })
        var rowInfoList = Array(Config.BOARD_HEIGHT, { i -> RowInfo(0, 0, 0, 0) })


        for (rowIndex in 0 until Config.BOARD_HEIGHT) {

            // create a string of the row for using regex
            var row = ""
            for (columnIndex in 0 until Config.BOARD_WITH) {
                val currentColor = board.grid[columnIndex][rowIndex]
                row += currentColor
            }

            log.info(row)

            var beginningSpaceRegex = Regex("(\\s+)($color+)\\s*")
            var middleSpaceRegex = Regex("($color+)(\\s+)($color+)")
            var endingSpaceRegex = Regex("($color+)(\\s+)")

            var startIndex = 0

            if(beginningSpaceRegex.containsMatchIn(row)) {
                beginningSpaceRegex.findAll(row, startIndex).forEach { matchResult ->
                    var groupValues = matchResult.groupValues

                    var spaceCount = groupValues[1].length
                    var coinsCount = groupValues[2].length

                    var index = matchResult.range.start + spaceCount - 1 // left of the coins
                    var score = coinsCount
                    var width = matchResult.range.endInclusive - matchResult.range.start + 1
                    if(score > scoreList[index] && width >= 4 && hasBottomCoin(index, rowIndex, board)){
                        scoreList[index] = score
                    }
                }
            }

            if(middleSpaceRegex.containsMatchIn(row)) {
                middleSpaceRegex.findAll(row, startIndex).forEach { matchResult ->
                    var groupValues = matchResult.groupValues
                    var coinsCount = groupValues[1].length + groupValues[3].length
                    var spaceCount = groupValues[2].length

                    var score = min(3, coinsCount) - (spaceCount - 1)
                    var index = matchResult.range.start + groupValues[1].length // right of the left coins group
                    var width = matchResult.range.endInclusive - matchResult.range.start + 1

                    if(score > scoreList[index] && width >= 4 && hasBottomCoin(index, rowIndex, board)){
                        scoreList[index] = score
                    }
                }
            }

            if(endingSpaceRegex.containsMatchIn(row)) {
                endingSpaceRegex.findAll(row, startIndex).forEach { matchResult ->
                    var groupValues = matchResult.groupValues
                    var spaceCount = groupValues[2].length
                    var coinsCount = groupValues[1].length

                    var score = coinsCount
                    var index = matchResult.range.start + coinsCount // right of the coins
                    var width = matchResult.range.endInclusive - matchResult.range.start + 1

                    if(score > scoreList[index] && width >= 4 && hasBottomCoin(index, rowIndex, board)){
                        scoreList[index] = score
                    }
                }
            }
        }

        return scoreList
    }

    fun hasBottomCoin(columnIndex: Int, rowIndex: Int, board: Board): Boolean {
        return rowIndex == 0 || !board.grid[columnIndex][rowIndex - 1].isBlank()
    }

}

data class RowInfo(var startIndex: Int,
                   var drawColIndex: Int,
                   var score: Int,
                   var count: Int)
