package com.noser.hackathon

import com.noser.hackathon.server.Board
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository


@Repository
class GameState {

    private val log = LoggerFactory.getLogger(this.javaClass)

    val gamesComputing = mutableMapOf<String, Board>()

    fun setComputing(board: Board) {
        if (!gamesComputing.containsKey(board.boardId)) {
            log.info("Adding ${board.boardId} to gamesComputing")
            gamesComputing[board.boardId] = board
        }
    }

    fun setDone(board: Board) {
        log.info("Removing ${board.boardId} from gamesComputing")
        gamesComputing.remove(board.boardId)
    }


}