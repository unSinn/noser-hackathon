package com.noser.hackathon


import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface GameServerAPI {

    @GET("hackathon/ninjas")
    fun getBoards(): Observable<Board>

    @GET("hackathon/ninjas/{name}")
    fun getBoard(@Path("name") name: String): Single<Board>
}

data class Board(val name: String, val pair: String)




