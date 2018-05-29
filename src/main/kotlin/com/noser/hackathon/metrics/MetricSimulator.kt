package com.noser.hackathon.metrics

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MetricSimulator(val gameMetrics: Metrics) {


    @Scheduled(fixedRate = 10000)
    fun runMatch() {
        gameMetrics.matchWon()
        gameMetrics.matchLost()
        if (Math.random() < 0.3) {
            gameMetrics.matchLost()
        } else {
            gameMetrics.matchWon()
        }
    }

    @Scheduled(fixedRate = 1000)
    fun runGame() {
        gameMetrics.startMatch()
        gameMetrics.startGame()
        gameMetrics.matchWon()
        gameMetrics.matchLost()
        if (Math.random() < 0.3) {
            gameMetrics.gameLost()
        } else {
            gameMetrics.gameWon()
        }
    }
}