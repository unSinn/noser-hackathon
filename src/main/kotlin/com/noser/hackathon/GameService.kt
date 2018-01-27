package com.noser.hackathon

import com.noser.hackathon.server.GameServer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class GameService(val state: GameState, final val server: GameServer, val player: AiService) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    init {
        startGame("Bösewicht")
        server.boards.subscribe { board ->
            log.info("Got Board $board")
            if (!board.boardStatus.gameFinished) {
                val playColumn = player.calculatePlayChip(board)
                server.play(board, playColumn)
                        .doOnError { log.error("Creating Play on Board ${board.boardId}", it) }
                        .subscribe()
            }

        }
    }

    final fun startGame(name: String) {
        server.createBoard(name).subscribe()
    }

    fun getBoards() {
        listOf(state.allBoards)
    }


}