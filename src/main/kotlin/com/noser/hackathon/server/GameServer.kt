package com.noser.hackathon.server

import com.noser.hackathon.Config.PLAYER_NAME
import com.noser.hackathon.Config.URL
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit.*


@Repository
class GameServer {

    private val api: GameServerAPI

    private val log = LoggerFactory.getLogger(this.javaClass)

    final val boards: Observable<Board>

    init {

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(1, SECONDS)
                .connectTimeout(1, SECONDS)
                .addInterceptor(logInterceptor)
                .build()

        val retrofit: Retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .baseUrl(URL)
                .build()


        api = retrofit.create(GameServerAPI::class.java)

        boards = Observable.interval(5, SECONDS)
                .flatMap { api.getBoards().flatMapIterable { it } }
                .doOnNext { log.info("Got new Board-Id $it") }
                .flatMap { api.getBoard(it) }
                .doOnError { log.error("Error getting Board", it) }

    }

    fun createBoard(enemy: String): Observable<Board> {
        log.info("Creating Board for Enemy $enemy")
        return api.createBoard(UUID.randomUUID().toString().substring(0, 6), BoardInfo(PLAYER_NAME, enemy, 0))
                .doOnError { log.error("Creating Board", it) }
    }

    fun play(board: Board, columnIndex: Int): Observable<Board> {
        val color: String = board.boardStatus.nextTurn ?: "x" // We play against ourselves, default Value is x
        return api.playChip(board.boardId, columnIndex, RequestBody.create(MediaType.parse("text/plain"), color))
                .doOnError { log.error("Error playing columne=$columnIndex on Board=${board.boardId} with Color=$color") }
                .doOnNext { log.info("Played columne=$columnIndex on Board=${board.boardId} with Color=$color") }
    }


}

