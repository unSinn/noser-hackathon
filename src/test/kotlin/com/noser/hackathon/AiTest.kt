package com.noser.hackathon

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
        val b = Board.fromString("""
                     |XXXXXXX
                     |OXOXOXO
                     |OOXOXOX
                     |OXOXOXO
                     |OOXOXOX
                     |OXOXOXO
                """.trimMargin(), info, status)
        ai.calculatePlay(b)
    }

}