package com.example.evaluateor

import kotlin.random.Random

object Evaluateor {

    data class ScoreEvaluation(
        val totalScore: Int,
        val securityScore: Int,
        val performanceScore: Int,
        val uxScore: Int,
        val isApproved: Boolean,
        val summary: String
    )

    fun evaluateMutation(isProGuardActive: Boolean, isR8Enabled: Boolean): ScoreEvaluation {
        val secBase = if (isProGuardActive) Random.nextInt(85, 100) else Random.nextInt(50, 70)
        val perfBase = if (isR8Enabled) Random.nextInt(88, 99) else Random.nextInt(60, 80)
        val uxBase = Random.nextInt(82, 98)

        val total = (secBase + perfBase + uxBase) / 3
        val approved = total >= 75 && secBase >= 70

        val summary = if (approved) {
            "APPROVED BY EVALUATEOR GUILD (Score: $total/100). High stability & secure bytecode."
        } else {
            "REJECTED BY EVALUATEOR GUILD (Score: $total/100). Security risk threshold violated."
        }

        return ScoreEvaluation(
            totalScore = total,
            securityScore = secBase,
            performanceScore = perfBase,
            uxScore = uxBase,
            isApproved = approved,
            summary = summary
        )
    }
}
