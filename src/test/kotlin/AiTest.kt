package com.noser.hackathon

import com.noser.hackathon.Config.BOARD_HEIGHT
import com.noser.hackathon.Config.BOARD_WITH
import com.noser.hackathon.server.Board
import com.noser.hackathon.server.BoardInfo
import com.noser.hackathon.server.BoardStatus
import com.noser.hackathon.server.Color
import org.junit.Test


class AiTest {

    val info = BoardInfo("a", "", 5)
    val status = BoardStatus(false, "", listOf(listOf()), Color.X)

    val ai = AiService()

    @Test
    fun testEmptyBoard() {

        val board = """
                     |XXXXXXX
                     |OXOXOXO
                     |OOXOXOX
                     |OXOXOXO
                     |OOXOXOX
                     |OXOXOXO
                """.trimMargin()

        val lines = board.split("\n")
        val grid = lines.map { l -> l.split("") }


        val flipped = Array(BOARD_WITH) { Array(BOARD_HEIGHT) { "-" } }

        for (row in 0 until Config.BOARD_HEIGHT) {
            for (column in 0 until Config.BOARD_WITH) {
                flipped[column][row] = grid[row][column]
            }
        }


        val b = Board("id", info, status, flipped)


    }

}