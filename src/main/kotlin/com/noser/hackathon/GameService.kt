package com.noser.hackathon

import com.noser.hackathon.Config.COMPUTATION_POOL
import com.noser.hackathon.Config.STATS_POOL
import com.noser.hackathon.server.Board
import com.noser.hackathon.server.Color
import com.noser.hackathon.server.GameServer
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import okhttp3.internal.Util.threadFactory
import retrofit2.HttpException
import java.util.concurrent.Executors.newFixedThreadPool
import retrofit2.adapter.rxjava2.Result.response


@Service
class GameService(val state: GameState, val server: GameServer, val player: AiService) {

    private val log = LoggerFactory.getLogger(this.javaClass)


    init {
        server.boards
                .observeOn(STATS_POOL)
                .doOnNext { state.setComputing(it) }
                .subscribe { board ->
                    computeParalell(board)
                }
    }

    private fun computeParalell(board: Board) {
        Observable.just(board)
                .subscribeOn(COMPUTATION_POOL)
                .map { it -> computePlay(it) }
                .subscribe()
    }

    private fun computePlay(board: Board) {
        log.info("Got playable Board $board")
        val color = board.boardStatus.nextTurn ?: Color.X // We play against ourselves, default Value is x
        val playColumn = player.calculatePlay(board)
        server.play(board, playColumn, color)
                .observeOn(STATS_POOL)
                .subscribeBy(
                        onNext = {
                            state.setDone(it)
                        },
                        onError = {
                            log.error("Error playing columne=$playColumn on Board=${board.boardId} with Color=$color   ERROR ::: ${it.message}")
                            if (it is HttpException) {
                                log.error("   BODY: ${it.response().errorBody()?.string()}")
                            }
                        }
                )
    }

}