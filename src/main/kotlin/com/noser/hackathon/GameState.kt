package com.noser.hackathon

import org.springframework.stereotype.Repository


@Repository
class GameState {

    val boards : MutableList<Board> = mutableListOf()


}