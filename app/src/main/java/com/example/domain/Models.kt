package com.example.domain

import androidx.compose.ui.graphics.vector.ImageVector

enum class DeployChannel {
    CANARY, STAGING, PRODUCTION
}

enum class AutoUpdateTrigger {
    ON_MUTATION, HOURLY, DAILY, MANUAL
}

enum class ToolTemplateCategory {
    WORK_AUTOMATION, FIELD_OPERATIONS, DATA_COLLECTION, TIME_TRACKING
}

data class WorkToolTemplate(
    val id: String,
    val name: String,
    val category: ToolTemplateCategory,
    val description: String,
    val defaultPackageName: String,
    val featureHighlights: List<String>,
    val defaultVersionName: String = "1.0.0"
)

data class ApkProject(
    val id: Long = 0,
    val name: String,
    val packageName: String,
    val templateId: String,
    val versionCode: Int = 100,
    val versionName: String = "1.0.0",
    val targetSdk: Int = 36,
    val isAutoDeployEnabled: Boolean = true,
    val deployChannel: DeployChannel = DeployChannel.PRODUCTION,
    val updateTrigger: AutoUpdateTrigger = AutoUpdateTrigger.ON_MUTATION,
    val isR8ShrinkingEnabled: Boolean = true,
    val isProGuardActive: Boolean = true,
    val activePatchCount: Int = 0,
    val lastUpdatedTime: Long = System.currentTimeMillis(),
    val status: String = "IDLE_MONITORING"
)

data class BuildMutation(
    val id: Long = 0,
    val projectId: Long,
    val title: String,
    val mutationType: String,
    val evaluateorScore: Int,
    val securityScore: Int,
    val performanceScore: Int,
    val uxScore: Int,
    val consensusApproved: Boolean,
    val appliedTimestamp: Long = System.currentTimeMillis(),
    val changelog: String
)

data class SwarmLog(
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val agentName: String,
    val action: String,
    val status: String, // PASSED, MUTATING, APPROVED, REJECTED
    val message: String
)
