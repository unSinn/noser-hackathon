package com.noser.hackathon.server


import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface GameServerAPI {


    @GET("connect-four/boards")
    fun getBoards(): Observable<List<String>>

    @GET("connect-four/teams/{team}/open")
    fun getOpenBoards(@Path("team") team: String): Observable<List<String>>

    @POST("connect-four/boards/{board}")
    fun createBoard(@Path("board") board: String,
                    @Body boardInfo: BoardInfo): Observable<Board>

    @POST("connect-four/boards/{board}/{column}")
    fun playChip(@Path("board") board: String,
                 @Path("column") column: Int,
                 @Body color: RequestBody): Observable<Board>

    @GET("connect-four/boards/{board}")
    fun getBoard(@Path("board") board: String): Observable<Board>

    @GET("connect-four/match/{team1}/{team2}/{round}")
    fun getMatch(@Path("team1") team1: String,
                 @Path("team2") team2: String,
                 @Path("round") round: String): Observable<Board>
}

enum class Color {
    X, O
}

data class Board(val boardId: String,
                 val boardInfo: BoardInfo,
                 val boardStatus: BoardStatus,
                 val grid: List<List<String>>)

data class BoardInfo(val playerX: String,
                     val playerO: String,
                     val round: Int)

data class BoardStatus(val gameFinished: Boolean,
                       val winner: String,
                       val winningPosition: List<List<Int>>,
                       val nextTurn: Color?)

data class Match(val round: Int,
                 val team1: String,
                 val team2: String,
                 val matches: Int)