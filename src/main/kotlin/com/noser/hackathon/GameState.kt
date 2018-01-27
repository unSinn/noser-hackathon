package com.noser.hackathon

import com.noser.hackathon.server.Board
import org.springframework.stereotype.Repository


@Repository
class GameState {

    val boards : MutableList<Board> = mutableListOf()


}