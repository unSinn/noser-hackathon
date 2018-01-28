package com.noser.hackathon

import com.noser.hackathon.server.Board
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(private val state: GameState) {


    @GetMapping("/start")
    fun startGame(@RequestParam(value = "name") name: String) {
        TODO("Input for tournament?")
    }

    @GetMapping("/gamesComputing")
    fun getGames(): Collection<Board> {
        return state.gamesComputing.values
    }

}