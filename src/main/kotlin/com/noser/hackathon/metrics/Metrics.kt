package com.noser.hackathon.metrics



interface Metrics {

    fun startMatch()

    fun startGame()

    fun matchWon()

    fun matchLost()

    fun gameWon()

    fun gameLost()

}