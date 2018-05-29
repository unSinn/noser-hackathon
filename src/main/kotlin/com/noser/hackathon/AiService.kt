package com.noser.hackathon

import com.noser.hackathon.Config.BOARD_WITH_MAX_INDEX
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
        val random = (0..BOARD_WITH_MAX_INDEX).random()
        log.info("Calculating for Board ${board.boardId} ...")
        //Thread.sleep(10000)
        log.info("Calculated random Play: Column $random for Board ${board.boardId}")
        return random
    }

}