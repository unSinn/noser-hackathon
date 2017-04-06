package com.noser.dojo

import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable

object Main {
    @JvmStatic fun main(args: Array<String>) {
        println(Messager().getMessage())

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
