package com.noser.hackathon.metrics

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class MetricSimulator(val gameMetrics: Metrics) {


    @Scheduled(fixedRate = 1000)
    fun runGame() {
        if (Math.random() < 0.3) {
            gameMetrics.gameLost()
        } else {
            gameMetrics.gameWon()
        }
    }
}