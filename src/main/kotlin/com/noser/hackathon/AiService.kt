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


        // getRandomAvailableColumn(board)
        return calculate("X", board)
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

}