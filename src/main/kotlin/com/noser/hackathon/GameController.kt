package com.noser.hackathon

import com.noser.hackathon.server.Board
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(private val state: GameState) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/start/{enemy}")
    fun startGame(@RequestParam(value = "enemy") enemy: String) {
        log.info("Starting Games vs $enemy")
    }

    @GetMapping("/gamesComputing")
    fun getGames(): Collection<Board> {
        return state.gamesComputing.values
    }

}