package com.noser.hackathon

import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers.io
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit.MILLISECONDS


@Service
class GameService(val state: GameState, server: GameServer) {

    private val log = LoggerFactory.getLogger(this.javaClass)


    init {
        server.boards()
                .subscribeOn(io())
                .timeout(100,MILLISECONDS)
                .retryWhen(RetryWithDelay(3, 2000))
                .subscribe(
                        { state.boards.add(it) },
                        { log.error("We got an error fetching boards.", it) },
                        { log.info("onComplete!") }
                )
    }

    fun start(name: String) {
        state.boards.add(Board(name, "b"))
    }

    fun getBoards() {
        listOf(state.boards)
    }


}