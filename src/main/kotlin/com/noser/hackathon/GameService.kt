package com.noser.hackathon

import com.noser.hackathon.server.GameServer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


@Service
class GameService(val state: GameState, server: GameServer) {

    private val log = LoggerFactory.getLogger(this.javaClass)


    init {
        server.createBoard("Bösewicht").subscribe()
        server.boards.subscribe {
            log.info("Got Board $it")
        }
    }

    fun start(name: String) {

    }

    fun getBoards() {
        listOf(state.boards)
    }


}