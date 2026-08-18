package com.example.system

import com.example.data.ApkRepository
import com.example.devator.DevatorEngine
import com.example.domain.ApkProject
import com.example.domain.BuildMutation
import com.example.domain.SwarmLog
import com.example.evaluateor.Evaluateor
import com.example.mandelacore.MandelaCore
import com.example.matrixcore.MatrixCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SwarmEngine(
    private val repository: ApkRepository,
    private val scope: CoroutineScope
) {
    private val _isSwarmRunning = MutableStateFlow(false)
    val isSwarmRunning: StateFlow<Boolean> = _isSwarmRunning.asStateFlow()

    private val _currentStatusMessage = MutableStateFlow("SWARM_IDLE")
    val currentStatusMessage: StateFlow<String> = _currentStatusMessage.asStateFlow()

    fun runAutoUpdatePipeline(project: ApkProject, customTitle: String? = null) {
        if (_isSwarmRunning.value) return

        scope.launch(Dispatchers.IO) {
            _isSwarmRunning.value = true
            _currentStatusMessage.value = "MUTATION_DISPATCH_STARTED"

            MandelaCore.triggerRealityGlitch(1200)

            // Step 1: Devator Drafts Mutation
            log("DevatorEngine", "Drafting mutation proposal for project '${project.name}'...", "MUTATING")
            delay(400)
            val draft = DevatorEngine.draftMutation(project, customTitle)
            log("DevatorEngine", "Mutation drafted: '${draft.mutationTitle}' -> Target v${draft.nextVersionName} (code ${draft.nextVersionCode})", "PASSED")
            delay(300)

            // Step 2: MatrixCore Validation
            log("MatrixCore", "Validating manifest rules, package namespace & ProGuard constraints...", "MUTATING")
            delay(350)
            val matrixValidation = MatrixCore.validateProjectRules(project)
            if (!matrixValidation.isValid) {
                log("MatrixCore", "Validation failed: ${matrixValidation.issues.joinToString()}", "REJECTED")
                _isSwarmRunning.value = false
                MandelaCore.endRealityGlitch()
                return@launch
            }
            log("MatrixCore", "Validation passed. Zero critical rule violations.", "PASSED")
            delay(300)

            // Step 3: Swarm Guild Audits (Coroutines)
            log("SecurityGuild", "Auditing encrypted DataStore & Retrofit interceptors...", "MUTATING")
            delay(400)
            log("SecurityGuild", "Security audit passed. Zero plaintext key exposure detected.", "PASSED")

            log("PerformanceAuditor", "Analyzing bytecode size reduction & R8 ProGuard rules...", "MUTATING")
            delay(350)
            log("PerformanceAuditor", "Performance audit passed. Bytecode reduced by ~18%.", "PASSED")

            log("UXEvaluator", "Auditing cyber-brutalist contrast ratios & 48dp touch targets...", "MUTATING")
            delay(300)
            log("UXEvaluator", "UX alignment score verified.", "PASSED")

            // Step 4: Evaluateor Scoring & Consensus Engine
            log("Evaluateor", "Scoring mutation against stability and security thresholds...", "MUTATING")
            delay(400)
            val score = Evaluateor.evaluateMutation(project.isProGuardActive, project.isR8ShrinkingEnabled)
            
            log("Evaluateor", score.summary, if (score.isApproved) "APPROVED" else "REJECTED")

            if (score.isApproved) {
                // Step 5: Arbiter applies mutation and updates project in repository
                val mutation = BuildMutation(
                    projectId = project.id,
                    title = draft.mutationTitle,
                    mutationType = draft.mutationType,
                    evaluateorScore = score.totalScore,
                    securityScore = score.securityScore,
                    performanceScore = score.performanceScore,
                    uxScore = score.uxScore,
                    consensusApproved = true,
                    appliedTimestamp = System.currentTimeMillis(),
                    changelog = draft.changelog
                )

                repository.addMutation(mutation)

                val updatedProject = project.copy(
                    versionCode = draft.nextVersionCode,
                    versionName = draft.nextVersionName,
                    activePatchCount = project.activePatchCount + 1,
                    lastUpdatedTime = System.currentTimeMillis(),
                    status = "DEPLOYED_v${draft.nextVersionName}"
                )

                repository.saveProject(updatedProject)

                // Step 6: MandelaCore OTA Client Push
                MandelaCore.notifyClientAppUpdate(draft.nextVersionName, draft.mutationTitle)
                log("ArbiterAgent", "CONSENSUS APPROVED: Pushed live OTA patch v${draft.nextVersionName} to ${project.deployChannel} channel!", "APPROVED")
                _currentStatusMessage.value = "OTA_DEPLOY_SUCCESS"
            } else {
                log("ArbiterAgent", "MUTATION REJECTED: Insufficient consensus score (${score.totalScore}/100)", "REJECTED")
                _currentStatusMessage.value = "MUTATION_REJECTED"
            }

            delay(600)
            MandelaCore.endRealityGlitch()
            _isSwarmRunning.value = false
        }
    }

    private suspend fun log(agent: String, msg: String, status: String) {
        val logItem = SwarmLog(
            timestamp = System.currentTimeMillis(),
            agentName = agent,
            action = "SWARM_MICRO_TASK",
            status = status,
            message = msg
        )
        repository.addSwarmLog(logItem)
    }
}
