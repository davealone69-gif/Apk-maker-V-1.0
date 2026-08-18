package com.example.matrixcore

import com.example.domain.ApkProject

object MatrixCore {

    data class ValidationResult(
        val isValid: Boolean,
        val issues: List<String>,
        val recommendations: List<String>
    )

    fun validateProjectRules(project: ApkProject): ValidationResult {
        val issues = mutableListOf<String>()
        val recs = mutableListOf<String>()

        if (!project.packageName.matches(Regex("^[a-z][a-z0-9_]*(\\.[a-z0-9_]+)+$"))) {
            issues.add("Invalid package format. Must follow reverse-domain standard.")
        }

        if (project.versionCode <= 0) {
            issues.add("versionCode must be greater than 0.")
        }

        if (!project.isProGuardActive) {
            recs.add("SECURITY WARNING: R8/ProGuard shrinking disabled. Enable for release build stability.")
        }

        if (project.targetSdk < 34) {
            recs.add("SDK NOTICE: Target SDK is below Android 14 standard.")
        }

        return ValidationResult(
            isValid = issues.isEmpty(),
            issues = issues,
            recommendations = recs
        )
    }

    fun verifyReleaseConsensus(
        securityScore: Int,
        performanceScore: Int,
        uxScore: Int,
        threshold: Int = 75
    ): Boolean {
        val average = (securityScore + performanceScore + uxScore) / 3
        return average >= threshold && securityScore >= 70
    }
}
