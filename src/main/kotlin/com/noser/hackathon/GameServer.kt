package com.noser.hackathon

import io.reactivex.Observable
import org.springframework.stereotype.Repository
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Repository
class GameServer {

    private val api: GameServerAPI

    init {
        val retrofit: Retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://10.25.100.61:8080/api/")
                .build()
        api = retrofit.create(GameServerAPI::class.java)
    }

    fun boards(): Observable<Board> {
        return api.getBoards()
    }


}

