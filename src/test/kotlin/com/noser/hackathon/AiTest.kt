package com.noser.hackathon

import com.noser.hackathon.server.Board
import com.noser.hackathon.server.BoardInfo
import com.noser.hackathon.server.BoardStatus
import com.noser.hackathon.server.Color
import org.junit.Assert
import org.junit.Test
import org.slf4j.LoggerFactory


class AiTest {

    private val log = LoggerFactory.getLogger(this.javaClass)

    val info = BoardInfo("a", "", 5)
    val status = BoardStatus(false, "", listOf(listOf()), Color.X)
    val color = Color.X

    val ai = AiService()

    @Test
    fun testFindAmountInColumn_empty() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                """.trimMargin(), info, status)
        assert(0 <= ai.calculatePlay(b, color))
        var expectedScoreList = Array(Config.BOARD_WITH, { i -> 0 })
        Assert.assertEquals(expectedScoreList, ai.findAmountInColumn("X", b))
        Assert.assertEquals(expectedScoreList, ai.findAmountInColumn("O", b))
    }

    @Test
    fun testFindAmountInColumn_full() {
        val b = Board.fromString("""
                     |XXXXXXX
                     |OXOXOXO
                     |OOXOXOX
                     |OXOXOXO
                     |OOXOXOX
                     |OXOXOXO
                """.trimMargin(), info, status)
        assert(0 == ai.calculatePlay(b, color))
        var expectedScoreList = Array(Config.BOARD_WITH, { i -> 0 })

        Assert.assertEquals(expectedScoreList, ai.findAmountInColumn("X", b))
        Assert.assertEquals(expectedScoreList, ai.findAmountInColumn("O", b))
    }

    @Test
    fun testFindAmountInColumn_scores() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |  XX OO
                     |XXXXOXO
                     |OXXOXOX
                     |OOOOOXO
                """.trimMargin(), info, status)
        var expectedScoreList = arrayOf(1, 2, 3, 2, 0, 0, 0)
        var scores = ai.findAmountInColumn("X", b)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(2 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_noScores() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |OXOXOXO
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 0, 0, 0, 0, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(0 <= ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_3_rightSpace() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |OO     ,
                     |XXX   O,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 0, 0, 3, 0, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(3 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_3_rightSpace_offset() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |OO     ,
                     |OOXXX O,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 0, 0, 0, 0, 3, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(5 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_2_rightSpace_offset() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |OOO    ,
                     |OOOXX  ,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 0, 0, 0, 0, 2, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(3 == ai.calculatePlay(b, color)) // block the enemy
    }

    @Test
    fun testFindHorizontal_3_leftSpace() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     | O     ,
                     | XXXOOO,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(3, 0, 0, 0, 0, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(0 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_3_leftSpace_offset() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     | O     ,
                     |OO XXXO,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 0, 3, 0, 0, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(2 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_2_leftSpace_offset() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |O      ,
                     |O  XXOO,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 0, 2, 0, 0, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(2 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_3_leftAndRighSpace() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     | O     ,
                     | XXX  O,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(3, 0, 0, 0, 3, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(0 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_2_leftAndRighSpace() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |      O,
                     | O    O,
                     | XX   O,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(2, 0, 0, 2, 0, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(6 == ai.calculatePlay(b, color)) // block enemy
    }

    @Test
    fun testFindHorizontal_3_leftAndRighSpace_offset() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |O XXX  ,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 3, 0, 0, 0, 3, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(1 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_middleSpace() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     | O     ,
                     |XX X   ,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 0, 3, 0, 1, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(2 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_middleSpace_scoreDown() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |     O ,
                     |     O ,
                     |XO  OOO,
                     |XX  XXO,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 0, 2, 2, 0, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(5 == ai.calculatePlay(b, color)) // block enemy
    }

    @Test
    fun testFindHorizontal_cascade() {
        val b = Board.fromString("""
                     |X      ,
                     |XX     ,
                     |XXX    ,
                     |OOOX   ,
                     |OOOXX  ,
                     |OOOXXX ,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 1, 2, 3, 1, 2, 3)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(3 == ai.calculatePlay(b, color))
    }

    @Test
    fun testFindHorizontal_noBottomCoin() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     |       ,
                     |OOOXX  ,
                     |OXOOO  ,
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)

        var expectedScoreList = arrayOf(0, 0, 0, 0, 0, 0, 0)
        scores.forEach { i -> log.info(i.toString()) }

        Assert.assertEquals(expectedScoreList, scores)
        assert(5 == ai.calculatePlay(b, color)) // block enemy
    }

    @Test
    fun testFindHorizontal() {
        val b = Board.fromString("""
                     |       ,
                     |       ,
                     |       ,
                     | XXX   ,
                     |OXOXX X
                     |OXOXOXO
                """.trimMargin(), info, status)

        var scores = ai.findInRow("X", b)
        scores.forEach { i -> log.info(i.toString()) }
        // assert(0 == ai.calculatePlay(b, color))
    }

}