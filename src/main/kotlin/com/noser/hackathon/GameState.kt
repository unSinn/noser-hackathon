package com.noser.hackathon

import com.noser.hackathon.server.Board
import org.springframework.stereotype.Repository


@Repository
class GameState {

    val allBoards: MutableList<Board> = mutableListOf()


}