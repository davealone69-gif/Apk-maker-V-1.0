package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.ApkProject
import com.example.domain.AutoUpdateTrigger
import com.example.domain.BuildMutation
import com.example.domain.DeployChannel
import com.example.domain.SwarmLog

@Entity(tableName = "apk_projects")
data class ApkProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val packageName: String,
    val templateId: String,
    val versionCode: Int,
    val versionName: String,
    val targetSdk: Int,
    val isAutoDeployEnabled: Boolean,
    val deployChannel: String,
    val updateTrigger: String,
    val isR8ShrinkingEnabled: Boolean,
    val isProGuardActive: Boolean,
    val activePatchCount: Int,
    val lastUpdatedTime: Long,
    val status: String
) {
    fun toDomain(): ApkProject = ApkProject(
        id = id,
        name = name,
        packageName = packageName,
        templateId = templateId,
        versionCode = versionCode,
        versionName = versionName,
        targetSdk = targetSdk,
        isAutoDeployEnabled = isAutoDeployEnabled,
        deployChannel = try { DeployChannel.valueOf(deployChannel) } catch (e: Exception) { DeployChannel.PRODUCTION },
        updateTrigger = try { AutoUpdateTrigger.valueOf(updateTrigger) } catch (e: Exception) { AutoUpdateTrigger.ON_MUTATION },
        isR8ShrinkingEnabled = isR8ShrinkingEnabled,
        isProGuardActive = isProGuardActive,
        activePatchCount = activePatchCount,
        lastUpdatedTime = lastUpdatedTime,
        status = status
    )

    companion object {
        fun fromDomain(project: ApkProject): ApkProjectEntity = ApkProjectEntity(
            id = project.id,
            name = project.name,
            packageName = project.packageName,
            templateId = project.templateId,
            versionCode = project.versionCode,
            versionName = project.versionName,
            targetSdk = project.targetSdk,
            isAutoDeployEnabled = project.isAutoDeployEnabled,
            deployChannel = project.deployChannel.name,
            updateTrigger = project.updateTrigger.name,
            isR8ShrinkingEnabled = project.isR8ShrinkingEnabled,
            isProGuardActive = project.isProGuardActive,
            activePatchCount = project.activePatchCount,
            lastUpdatedTime = project.lastUpdatedTime,
            status = project.status
        )
    }
}

@Entity(tableName = "build_mutations")
data class BuildMutationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val title: String,
    val mutationType: String,
    val evaluateorScore: Int,
    val securityScore: Int,
    val performanceScore: Int,
    val uxScore: Int,
    val consensusApproved: Boolean,
    val appliedTimestamp: Long,
    val changelog: String
) {
    fun toDomain(): BuildMutation = BuildMutation(
        id = id,
        projectId = projectId,
        title = title,
        mutationType = mutationType,
        evaluateorScore = evaluateorScore,
        securityScore = securityScore,
        performanceScore = performanceScore,
        uxScore = uxScore,
        consensusApproved = consensusApproved,
        appliedTimestamp = appliedTimestamp,
        changelog = changelog
    )

    companion object {
        fun fromDomain(mutation: BuildMutation): BuildMutationEntity = BuildMutationEntity(
            id = mutation.id,
            projectId = mutation.projectId,
            title = mutation.title,
            mutationType = mutation.mutationType,
            evaluateorScore = mutation.evaluateorScore,
            securityScore = mutation.securityScore,
            performanceScore = mutation.performanceScore,
            uxScore = mutation.uxScore,
            consensusApproved = mutation.consensusApproved,
            appliedTimestamp = mutation.appliedTimestamp,
            changelog = mutation.changelog
        )
    }
}

@Entity(tableName = "swarm_logs")
data class SwarmLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val agentName: String,
    val action: String,
    val status: String,
    val message: String
) {
    fun toDomain(): SwarmLog = SwarmLog(
        id = id,
        timestamp = timestamp,
        agentName = agentName,
        action = action,
        status = status,
        message = message
    )

    companion object {
        fun fromDomain(log: SwarmLog): SwarmLogEntity = SwarmLogEntity(
            id = log.id,
            timestamp = log.timestamp,
            agentName = log.agentName,
            action = log.action,
            status = log.status,
            message = log.message
        )
    }
}
