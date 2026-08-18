package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ApkRepository
import com.example.devator.DevatorEngine
import com.example.domain.ApkProject
import com.example.domain.AutoUpdateTrigger
import com.example.domain.BuildMutation
import com.example.domain.DefaultTemplates
import com.example.domain.DeployChannel
import com.example.domain.SwarmLog
import com.example.mandelacore.MandelaCore
import com.example.mandelacore.MandelaUIRealityState
import com.example.system.SwarmEngine
import com.example.domain.AiModelInfo
import com.example.domain.FreeAiModelCatalog
import com.example.system.DeviceHardwareReport
import com.example.system.NetworkPingResult
import com.example.system.CryptoHashResult
import com.example.system.RealDeviceTelemetryProvider
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ApkRepository
    val swarmEngine: SwarmEngine

    val allProjects: StateFlow<List<ApkProject>>
    val allMutations: StateFlow<List<BuildMutation>>
    val swarmLogs: StateFlow<List<SwarmLog>>
    val realityState: StateFlow<MandelaUIRealityState> = MandelaCore.realityState

    private val _selectedProject = MutableStateFlow<ApkProject?>(null)
    val selectedProject: StateFlow<ApkProject?> = _selectedProject.asStateFlow()

    private val _activeTab = MutableStateFlow(0)
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    // Real Hardware & Telemetry State
    private val _deviceHardwareReport = MutableStateFlow<DeviceHardwareReport?>(null)
    val deviceHardwareReport: StateFlow<DeviceHardwareReport?> = _deviceHardwareReport.asStateFlow()

    private val _pingResult = MutableStateFlow<NetworkPingResult?>(null)
    val pingResult: StateFlow<NetworkPingResult?> = _pingResult.asStateFlow()

    private val _isPinging = MutableStateFlow(false)
    val isPinging: StateFlow<Boolean> = _isPinging.asStateFlow()

    private val _cryptoChecksums = MutableStateFlow<List<CryptoHashResult>>(emptyList())
    val cryptoChecksums: StateFlow<List<CryptoHashResult>> = _cryptoChecksums.asStateFlow()

    // Real On-Device APK Builder State
    private val _generatedApkArtifact = MutableStateFlow<com.example.system.GeneratedApkArtifact?>(null)
    val generatedApkArtifact: StateFlow<com.example.system.GeneratedApkArtifact?> = _generatedApkArtifact.asStateFlow()

    private val _isBuildingApk = MutableStateFlow(false)
    val isBuildingApk: StateFlow<Boolean> = _isBuildingApk.asStateFlow()

    private val _apkBuildProgress = MutableStateFlow<com.example.system.ApkBuildProgress?>(null)
    val apkBuildProgress: StateFlow<com.example.system.ApkBuildProgress?> = _apkBuildProgress.asStateFlow()

    // AI Training Facility State
    private val _activeAiModel = MutableStateFlow<AiModelInfo>(FreeAiModelCatalog.models.first())
    val activeAiModel: StateFlow<AiModelInfo> = _activeAiModel.asStateFlow()

    private val _geminiApiKey = MutableStateFlow<String>("")
    val geminiApiKey: StateFlow<String> = _geminiApiKey.asStateFlow()

    private val _isInferring = MutableStateFlow(false)
    val isInferring: StateFlow<Boolean> = _isInferring.asStateFlow()

    private val _inferenceOutput = MutableStateFlow<String>("")
    val inferenceOutput: StateFlow<String> = _inferenceOutput.asStateFlow()

    private val _inferenceLatencyMs = MutableStateFlow<Long>(0L)
    val inferenceLatencyMs: StateFlow<Long> = _inferenceLatencyMs.asStateFlow()

    private val _inferenceSpeedTps = MutableStateFlow<Int>(0)
    val inferenceSpeedTps: StateFlow<Int> = _inferenceSpeedTps.asStateFlow()

    private val _isRealApiCall = MutableStateFlow(false)
    val isRealApiCall: StateFlow<Boolean> = _isRealApiCall.asStateFlow()

    // Exported Source Code State
    private val _exportedSource = MutableStateFlow<com.example.domain.ProjectCodeExporter.ExportedProjectSource?>(null)
    val exportedSource: StateFlow<com.example.domain.ProjectCodeExporter.ExportedProjectSource?> = _exportedSource.asStateFlow()

    init {
        val dao = AppDatabase.getDatabase(application).apkDao()
        repository = ApkRepository(dao)
        swarmEngine = SwarmEngine(repository, viewModelScope)

        allProjects = repository.allProjects.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allMutations = repository.allMutations.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        swarmLogs = repository.swarmLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Initialize Real Hardware Diagnostics & Checksum Baseline
        refreshHardwareDiagnostics()
        computeStringChecksums("Devator Monorepo Swarm Consensus Ledger v1.0.0")

        // Seed initial default project if empty
        viewModelScope.launch {
            allProjects.collect { projects ->
                if (projects.isEmpty()) {
                    val defaultTemplate = DefaultTemplates.list.first()
                    val initialProject = ApkProject(
                        name = "Field Work Auto-Form",
                        packageName = defaultTemplate.defaultPackageName,
                        templateId = defaultTemplate.id,
                        versionCode = 100,
                        versionName = "1.0.0",
                        targetSdk = 36,
                        isAutoDeployEnabled = true,
                        deployChannel = DeployChannel.PRODUCTION,
                        updateTrigger = AutoUpdateTrigger.ON_MUTATION,
                        isR8ShrinkingEnabled = true,
                        isProGuardActive = true,
                        status = "IDLE_MONITORING"
                    )
                    val id = repository.saveProject(initialProject)
                    _selectedProject.value = initialProject.copy(id = id)
                } else if (_selectedProject.value == null) {
                    _selectedProject.value = projects.first()
                }
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }

    fun selectProject(project: ApkProject) {
        _selectedProject.value = project
    }

    fun createProjectFromTemplate(
        templateId: String,
        customName: String,
        packageName: String,
        channel: DeployChannel
    ) {
        viewModelScope.launch {
            val template = DefaultTemplates.list.find { it.id == templateId } ?: DefaultTemplates.list.first()
            val newProject = ApkProject(
                name = customName.ifBlank { template.name },
                packageName = packageName.ifBlank { template.defaultPackageName },
                templateId = template.id,
                versionCode = 100,
                versionName = template.defaultVersionName,
                deployChannel = channel,
                isAutoDeployEnabled = true,
                status = "INITIALIZED"
            )
            val newId = repository.saveProject(newProject)
            val saved = newProject.copy(id = newId)
            _selectedProject.value = saved

            // Trigger initial auto-deploy mutation pipeline
            swarmEngine.runAutoUpdatePipeline(saved, "Initial Auto-Build & Baseline Deployment")
        }
    }

    fun triggerProjectAutoUpdate(project: ApkProject, customChangelogNote: String? = null) {
        swarmEngine.runAutoUpdatePipeline(project, customChangelogNote)
    }

    fun refreshHardwareDiagnostics() {
        viewModelScope.launch(Dispatchers.IO) {
            val report = RealDeviceTelemetryProvider.inspectHardware(getApplication())
            _deviceHardwareReport.value = report
        }
    }

    fun executeNetworkPing(url: String = "https://www.google.com/generate_204") {
        viewModelScope.launch {
            _isPinging.value = true
            val res = RealDeviceTelemetryProvider.executeHttpPing(url)
            _pingResult.value = res
            _isPinging.value = false
        }
    }

    fun computeStringChecksums(input: String) {
        if (input.isBlank()) return
        _cryptoChecksums.value = RealDeviceTelemetryProvider.computeChecksums(input)
    }

    fun buildSignedApkLocally(context: Context, project: ApkProject) {
        viewModelScope.launch {
            _isBuildingApk.value = true
            _apkBuildProgress.value = com.example.system.ApkBuildProgress("STARTING", 5, "Preparing local compilation pipeline...")
            try {
                val artifact = com.example.system.LocalApkBuilder.buildSignedApk(
                    context = context,
                    project = project
                ) { progress ->
                    _apkBuildProgress.value = progress
                }
                _generatedApkArtifact.value = artifact
                computeStringChecksums(artifact.sha256Checksum)
            } catch (e: Exception) {
                e.printStackTrace()
                _apkBuildProgress.value = com.example.system.ApkBuildProgress("ERROR", 0, "Build failed: ${e.localizedMessage ?: e.message}")
            } finally {
                _isBuildingApk.value = false
            }
        }
    }

    fun installGeneratedApk(context: Context) {
        val artifact = _generatedApkArtifact.value ?: return
        try {
            com.example.system.LocalApkBuilder.installApkOnDevice(context, artifact)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareGeneratedApk(context: Context) {
        val artifact = _generatedApkArtifact.value ?: return
        try {
            com.example.system.LocalApkBuilder.shareApkFile(context, artifact)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun shareProjectZip(context: Context, project: ApkProject) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val (_, uri) = com.example.domain.ProjectCodeExporter.createProjectZipFile(context, project)
                withContext(Dispatchers.Main) {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/zip"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        putExtra(Intent.EXTRA_SUBJECT, "${project.name} Android Monorepo Project ZIP")
                        putExtra(Intent.EXTRA_TEXT, "Exported production-ready Android Studio project for ${project.name} (${project.packageName})")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    val chooser = Intent.createChooser(shareIntent, "Share ${project.name} Project ZIP").apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(chooser)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateProjectSettings(updatedProject: ApkProject) {
        viewModelScope.launch {
            repository.saveProject(updatedProject)
            _selectedProject.value = updatedProject
        }
    }

    fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            if (_selectedProject.value?.id == projectId) {
                _selectedProject.value = allProjects.value.firstOrNull { it.id != projectId }
            }
        }
    }

    fun clearLogs() {
        viewModelScope.launch {
            repository.clearLogs()
        }
    }

    fun setGeminiApiKey(key: String) {
        _geminiApiKey.value = key
    }

    fun generateProjectExportSource(project: ApkProject) {
        _exportedSource.value = com.example.domain.ProjectCodeExporter.generateCompleteProjectSource(project)
    }

    fun clearExportedSource() {
        _exportedSource.value = null
    }

    fun setActiveAiModel(model: AiModelInfo) {
        _activeAiModel.value = model
    }

    fun runAiInference(model: AiModelInfo, userPrompt: String) {
        if (userPrompt.isBlank()) return
        viewModelScope.launch {
            _isInferring.value = true
            _inferenceOutput.value = "CONNECTING TO ENGINE [${model.name}]...\n"
            
            _inferenceOutput.value += "> INFERENCE TARGET: ${model.name} (${model.provider})\n"
            _inferenceOutput.value += "> SPECIALTY: ${model.specialty}\n"
            _inferenceOutput.value += "> PROMPT: \"$userPrompt\"\n\n"

            val result = com.example.data.GeminiApiClient.generateContent(
                prompt = userPrompt,
                apiKey = _geminiApiKey.value.ifBlank { null },
                modelId = if (model.isGeminiRestCapable) "gemini-2.5-flash" else model.id
            )

            _isRealApiCall.value = result.isRealApiCall
            _inferenceLatencyMs.value = result.latencyMs
            _inferenceSpeedTps.value = if (result.latencyMs > 0) {
                ((result.tokenCountEstimated * 1000L) / result.latencyMs).toInt().coerceAtLeast(1)
            } else {
                model.speedTokensPerSec
            }

            val header = if (result.isRealApiCall) {
                if (result.isSuccess) {
                    "[REAL CLOUD API INFERENCE - GOOGLE GEMINI]\n" +
                    "> Model: ${result.modelUsed} | Latency: ${result.latencyMs}ms | Tokens: ~${result.tokenCountEstimated}\n\n"
                } else {
                    "[CLOUD API ERROR - FALLBACK ENGAGED]\n" +
                    "> Error: ${result.errorMessage}\n\n"
                }
            } else {
                "[LOCAL DETERMINISTIC HEURISTIC ENGINE]\n" +
                "> Note: No Gemini API Key supplied. Generated via zero-network local Kotlin engine.\n" +
                "> Configure your API key above to dispatch live Cloud LLM requests.\n\n"
            }

            _inferenceOutput.value = header + result.outputText
            _isInferring.value = false
        }
    }
}
