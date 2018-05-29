package com.noser.hackathon.metrics

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class MetricsImpl(meterRegistry: MeterRegistry) : Metrics {

    private val startMatch: Counter = meterRegistry.counter("matches_started_total")
    private val startGame: Counter = meterRegistry.counter("games_started_total")
    private val matchWon: Counter = meterRegistry.counter("matches_won_total")
    private val matchLost: Counter = meterRegistry.counter("matches_lost_total")
    private val gameWon: Counter = meterRegistry.counter("games_won_total")
    private val gameLost: Counter = meterRegistry.counter("games_lost_total")

    override fun startMatch() {
        startMatch.increment()
    }

    override fun startGame() {
        startGame.increment()
    }

    override fun matchWon() {
        matchWon.increment()
    }

    override fun matchLost() {
        matchLost.increment()
    }

    override fun gameWon() {
        gameWon.increment()
    }

    override fun gameLost() {
        gameLost.increment();
    }

}