package com.noser.dojo

import io.reactivex.Observable
import io.reactivex.Observable.*
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object Main {
    @JvmStatic fun main(args: Array<String>) {
        println(Messager().getMessage())

        // rxList()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()


        val service = retrofit.create(RestAPI::class.java)

        service.listRepos("unSinn")
                .flatMap { list -> fromIterable(list) }
                .subscribeBy(
                        onNext = ::println,
                        onError = { it.printStackTrace() },
                        onComplete = { println("Done!") }
                )
    }

    private fun rxList() {
        val list = listOf("Alpha", "Beta", "Gamma", "Delta", "Epsilon")

        list.toObservable()
                .filter { it.length >= 5 }
                .repeat(2)
                .subscribeBy(
                        onNext = ::println,
                        onError = { it.printStackTrace() },
                        onComplete = { println("Done!") }
                )
    }
}
