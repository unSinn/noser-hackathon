package com.noser.hackathon

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl("http://10.25.100.61:8080/api/")
        .build()


val service: GameServerAPI = retrofit.create(GameServerAPI::class.java)

object Main {
    @JvmStatic
    fun main(args: Array<String>) {

        service.getNinjas().forEach { println(it) }


        println("done")
    }
}

