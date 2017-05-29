package com.noser.dojo

import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers.io
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.system.exitProcess

val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl("http://10.25.100.61:8080/api/")
        .build()


val service: RestAPI = retrofit.create(RestAPI::class.java)

object Main {
    @JvmStatic fun main(args: Array<String>) {

        val latch = CountDownLatch(1)

        service.getNinjas()
                .doOnNext { println("trying to get Ninjas") }
                .observeOn(io())
                .subscribeOn(io())
                .timeout(100, MILLISECONDS)
                .retry(50)
                .flatMapIterable { it }
                .doOnNext { println(it) }
                .flatMap { ninja ->
                    service.getNinja(ninja)
                            .doOnNext { println("trying to get Ninja $ninja") }
                            .timeout(100, MILLISECONDS)
                            .retry(50)
                            .observeOn(io())
                            .doOnNext { println(it) }
                }
                .toList()
                .subscribeBy(
                        onSuccess = {
                            println(it)
                            latch.countDown()
                        },
                        onError = {
                            println("we are fucked")
                            it.printStackTrace()
                            exitProcess(5)
                        }
                )

        latch.await()
        println("done")
    }
}

