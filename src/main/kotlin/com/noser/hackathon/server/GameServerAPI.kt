package com.noser.hackathon.server


import com.noser.hackathon.Config
import com.noser.hackathon.Config.BOARD_HEIGHT
import com.noser.hackathon.Config.BOARD_WITH
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.*


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
                 val grid: Array<Array<String>>) {
    companion object {
        fun fromString(s: String, info: BoardInfo, status: BoardStatus): Board {
            val lines = s.split("\n")
            val grid = lines.map { l -> l.split("") }


            val flipped = Array(BOARD_WITH) { Array(BOARD_HEIGHT) { "-" } }

            for (row in 0 until BOARD_HEIGHT) {
                for (column in 0 until Config.BOARD_WITH) {
                    flipped[column][row] = grid[BOARD_HEIGHT - 1 - row][column + 1]
                }
            }


            return Board("id", info, status, flipped)
        }
    }
    override fun toString(): String {
        val sb = StringBuilder()

        for (row in BOARD_HEIGHT - 1 downTo 0) {
            for (column in 0 until BOARD_WITH) {
                sb.append(grid[column][row])
            }
            sb.append("\n")
        }

        return "$boardId ${boardInfo.playerO} vs ${boardInfo.playerX} round:${boardInfo.round} \n" +
                "finished: ${boardStatus.gameFinished}, nextTurn: ${boardStatus.nextTurn}\n" +
                sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (boardId != other.boardId) return false
        if (boardInfo != other.boardInfo) return false
        if (boardStatus != other.boardStatus) return false
        if (!Arrays.equals(grid, other.grid)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = boardId.hashCode()
        result = 31 * result + boardInfo.hashCode()
        result = 31 * result + boardStatus.hashCode()
        result = 31 * result + Arrays.hashCode(grid)
        return result
    }
}

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