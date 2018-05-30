package com.noser.hackathon

import com.noser.hackathon.server.Board
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Service
import java.util.*


fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) + start

@Service
@EnableScheduling
class AiService {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun calculatePlay(board: Board): Int {
        log.info("Calculating for Board ${board.boardId} ...")


        var index = calculate("X", board)

        if(index < 0){
            return getRandomAvailableColumn()
        }
    }

    fun getRandomAvailableColumn(board: Board): Int {
        val availableColumns = mutableListOf<Int>()

        board.grid.forEachIndexed{ index, column ->
            if(column[Config.BOARD_HEIGHT -1 ].isBlank()) {
                availableColumns.add(index)
            }
        }

        var column = 0

        if(availableColumns.size <= 0){
            log.info("No more space available! All columns are full")
        } else {
            val random = (0 until availableColumns.size).random()
            column = availableColumns[random]
            log.info("Calculated random Play: Column $column for Board ${board.boardId}")
        }

        return column
    }

    fun calculate(color: String, board: Board): Int {
        // find 3 in a column of our own color
        var scores = findAmountInColumn(color, board, 3)
        if(scores.max() == 3) {
            return scores.indexOf(3)
        }

        return -1
    }

    fun findAmountInColumn(color: String, board: Board, amount: Int): Array<Int> {
        var count = 0
        var scoreList = Array(Config.BOARD_WITH, {i -> 0} )

        board.grid.forEachIndexed{ columnIndex, column ->
            count = 0

            column.forEachIndexed{ rowIndex, currentColor ->
                if (currentColor == color){
                    // count chosen color
                    count++
                } else if(currentColor.isBlank()) {
                    // no mor coins
                    if(rowIndex < Config.BOARD_HEIGHT-1){
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
        var spaceCount = 0
        var scoreList = Array(Config.BOARD_WITH, {i -> 0} )
        var rowScoreList = Array(Config.BOARD_HEIGHT, { i -> 0})
        var rowInfoList = Array(Config.BOARD_HEIGHT, { i -> RowInfo(0, 0, 0, 0, 0)})
        var spaceCountBefore = 0
        var spaceCountAfter = 0
        var spaceCountMiddle = 0
        var startIndex = -1

        for(rowIndex in 0 until Config.BOARD_HEIGHT) {
            for(columnIndex in 0 until Config.BOARD_WITH) {
                val currentColor = board.grid[columnIndex][rowIndex]
                if(currentColor == color) {
                    count++
                    if(spaceCountAfter > 0){
                        // we found the color again, swap 'After' and 'Middle'
                        spaceCountMiddle = spaceCountAfter
                        spaceCountAfter = 0
                    }
                } else if(currentColor.isBlank()) {
                    if(hasBottomCoin(columnIndex, rowIndex, board)) {
                        if (count <= 0) {
                            spaceCountBefore++
                        } else {
                            spaceCountAfter++
                        }
                    }
                } else {
                    // other color
                    val width = spaceCountBefore + spaceCountMiddle + spaceCountAfter + count
                    if(width >= 4){
                        val verticalScore = count
                        if( rowScoreList[rowIndex] < verticalScore){
                            rowScoreList[rowIndex] = verticalScore
                            rowInfoList[rowIndex].startIndex = columnIndex - width
                            rowInfoList[rowIndex].spaceCountBefore = spaceCountBefore
                            rowInfoList[rowIndex].spaceCountMiddle = spaceCountMiddle
                            rowInfoList[rowIndex].spaceCountAfter = spaceCountAfter
                            rowInfoList[rowIndex].count = count
                        }
                    }

                    spaceCountBefore = 0
                    spaceCountMiddle = 0
                    spaceCountAfter = 0
                    count = 0
                }
            }
        }

        var max = rowScoreList.max()
        var rowIndex = rowScoreList.indexOf(max)

        var rowInfo = rowInfoList[rowIndex]

        var colIndex = 0
        if(rowInfo.spaceCountMiddle == 1) {
            colIndex = rowInfo.startIndex + rowInfo.spaceCountBefore
        }

        scoreList[colIndex] = rowScoreList[rowIndex];
        return scoreList
    }

    fun hasBottomCoin(columnIndex: Int, rowIndex: Int, board: Board): Boolean {
        return rowIndex >= 1 && !board.grid[columnIndex][rowIndex-1].isBlank()
    }

}

data class RowInfo(var startIndex: Int,
                 var spaceCountBefore: Int,
                 var spaceCountMiddle: Int,
                   var spaceCountAfter: Int,
                   var count: Int)
