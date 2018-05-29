package com.noser.hackathon

import io.reactivex.schedulers.Schedulers
import okhttp3.internal.Util.threadFactory
import java.util.concurrent.Executors.newFixedThreadPool

object Config {

    val BOARD_WITH = 7
    val BOARD_WITH_MAX_INDEX = BOARD_WITH - 1
    val BOARD_HEIGHT = 6

    val GENOSSEN = "Genossen"
    //val URL = "https://noser-connect-four.azurewebsites.net/api/"
    val URL = "http://localhost:8080/api/"

    val STATS_POOL = Schedulers.from(newFixedThreadPool(1, threadFactory("StatSched-", true)))
    val COMPUTATION_POOL = Schedulers.from(newFixedThreadPool(8, threadFactory("ComputationSched-", true)))
}