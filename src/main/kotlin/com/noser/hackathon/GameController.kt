package com.noser.hackathon

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(private val service: GameService) {


    @GetMapping("/start")
    fun startGame(@RequestParam(value = "name") name: String) {
        service.start(name)
    }

    @GetMapping("/games")
    fun getGames() {
        return service.getBoards()
    }

}