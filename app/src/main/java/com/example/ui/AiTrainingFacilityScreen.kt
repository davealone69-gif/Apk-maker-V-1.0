package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.AiModelInfo
import com.example.domain.FreeAiModelCatalog
import com.example.ui.theme.CyberBorderLine
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberErrorRed
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonLime
import com.example.ui.theme.CyberObsidian
import com.example.ui.theme.CyberSurfaceDark
import com.example.ui.theme.CyberTerminalGreen
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.CyberYellow

@Composable
fun AiTrainingFacilityScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeModel by viewModel.activeAiModel.collectAsState()
    val apiKey by viewModel.geminiApiKey.collectAsState()
    val isInferring by viewModel.isInferring.collectAsState()
    val inferenceOutput by viewModel.inferenceOutput.collectAsState()
    val latencyMs by viewModel.inferenceLatencyMs.collectAsState()
    val speedTps by viewModel.inferenceSpeedTps.collectAsState()
    val isRealApiCall by viewModel.isRealApiCall.collectAsState()
    val realityState by viewModel.realityState.collectAsState()

    var apiKeyInput by remember(apiKey) { mutableStateOf(apiKey) }
    var testPromptInput by remember { mutableStateOf("Generate cyber-brutalist Jetpack Compose state card for auto-deploy pipeline") }
    var selectedFacilityModel by remember { mutableStateOf(activeModel) }
    val clipboardManager = LocalClipboardManager.current
    var copiedInferenceToast by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlitchHeader(
                title = "AI & LLM TRAINING LAB",
                subtitle = "Google Gemini REST API Integration • Real-Time Code Synthesis",
                isGlitchActive = realityState.isGlitchActive
            )
        }

        // Live API Configuration Card
        item {
            CyberCard(
                borderColor = if (apiKey.isNotBlank()) CyberNeonLime else CyberYellow,
                title = "GEMINI CLOUD API INTEGRATION",
                badgeText = if (apiKey.isNotBlank()) "ONLINE CLOUD READY" else "LOCAL HEURISTIC ONLY",
                badgeColor = if (apiKey.isNotBlank()) CyberNeonLime else CyberYellow
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (apiKey.isNotBlank())
                            "Gemini API key is configured. Real HTTP REST requests will be dispatched to Google Generative Language endpoints."
                        else
                            "No API key entered. The system operates in Local Heuristic Engine mode. Enter a Gemini API Key to enable live cloud LLM inference.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyberTextSecondary
                    )

                    OutlinedTextField(
                        value = apiKeyInput,
                        onValueChange = { 
                            apiKeyInput = it
                            viewModel.setGeminiApiKey(it)
                        },
                        label = { Text("Google Gemini API Key (Optional)") },
                        placeholder = { Text("AIzaSy...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("gemini_api_key_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonLime,
                            unfocusedBorderColor = CyberBorderLine,
                            focusedLabelColor = CyberNeonLime,
                            unfocusedLabelColor = CyberTextSecondary
                        )
                    )
                }
            }
        }

        // Active Engine Status Banner
        item {
            CyberCard(
                borderColor = CyberNeonCyan,
                title = "ACTIVE DEVATOR SWARM ENGINE",
                badgeText = activeModel.name.uppercase(),
                badgeColor = CyberNeonLime
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Provider: ${activeModel.provider} (${activeModel.tier})",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CyberTextSecondary
                        )
                        Text(
                            text = "Specialty: ${activeModel.specialty}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CyberNeonCyan,
                            fontSize = 12.sp
                        )
                    }
                    CyberBadge(
                        text = "${activeModel.speedTokensPerSec} TOKENS/SEC",
                        color = CyberYellow
                    )
                }
            }
        }

        // Model Selector Ribbon
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "SELECT FREE AI / LLM MODEL ENGINE:",
                    style = MaterialTheme.typography.labelMedium,
                    color = CyberNeonLime,
                    fontWeight = FontWeight.Bold
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(FreeAiModelCatalog.models) { model ->
                        val isSelected = selectedFacilityModel.id == model.id
                        val isActiveSwarm = activeModel.id == model.id

                        Box(
                            modifier = Modifier
                                .clip(CutCornerShape(4.dp))
                                .background(if (isSelected) CyberSurfaceDark else CyberObsidian)
                                .border(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) CyberNeonCyan else CyberBorderLine,
                                    CutCornerShape(4.dp)
                                )
                                .clickable { selectedFacilityModel = model }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = model.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) CyberNeonCyan else CyberTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = model.provider,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CyberTextSecondary,
                                    fontSize = 10.sp
                                )
                                if (isActiveSwarm) {
                                    CyberBadge(text = "SWARM ACTIVE", color = CyberNeonLime)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Selected Model Detail Card & Action
        item {
            CyberCard(
                borderColor = CyberYellow,
                title = "MODEL SPECIFICATIONS: ${selectedFacilityModel.name}",
                badgeText = selectedFacilityModel.tier,
                badgeColor = CyberYellow
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = selectedFacilityModel.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyberTextPrimary
                    )

                    HorizontalDivider(color = CyberBorderLine)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Context Window:", style = MaterialTheme.typography.labelMedium, color = CyberTextSecondary)
                            Text(selectedFacilityModel.contextWindow, style = MaterialTheme.typography.labelLarge, color = CyberNeonCyan, fontFamily = FontFamily.Monospace)
                        }
                        Column {
                            Text("Speed Throughput:", style = MaterialTheme.typography.labelMedium, color = CyberTextSecondary)
                            Text("${selectedFacilityModel.speedTokensPerSec} tok/s", style = MaterialTheme.typography.labelLarge, color = CyberNeonLime, fontFamily = FontFamily.Monospace)
                        }
                        Column {
                            Text("Evaluateor Rating:", style = MaterialTheme.typography.labelMedium, color = CyberTextSecondary)
                            Text("${selectedFacilityModel.evaluateorScore}/100", style = MaterialTheme.typography.labelLarge, color = CyberMagenta, fontFamily = FontFamily.Monospace)
                        }
                    }

                    if (activeModel.id != selectedFacilityModel.id) {
                        CyberButton(
                            text = "SET AS ACTIVE DEVATOR SWARM ENGINE",
                            onClick = {
                                viewModel.setActiveAiModel(selectedFacilityModel)
                            },
                            color = CyberNeonLime,
                            icon = Icons.Default.CheckCircle,
                            testTagStr = "set_active_swarm_engine_button"
                        )
                    }
                }
            }
        }

        // Interactive Prompt Playground & Training Facility Output
        item {
            CyberCard(
                borderColor = CyberMagenta,
                title = "MODEL PROMPT PLAYGROUND & TRAINING LAB"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Execute test inference prompt against '${selectedFacilityModel.name}':",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyberTextSecondary
                    )

                    // Quick Prompt Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "Compose Card UI" to "Generate cyber-brutalist Jetpack Compose state card for auto-deploy pipeline",
                            "Security Hardening" to "Audit AndroidManifest.xml and ProGuard rules for sensitive token leakage",
                            "Room DB Schema" to "Write Kotlin Room Entity and Dao for fast offline caching"
                        ).forEach { (chipLabel, promptValue) ->
                            Box(
                                modifier = Modifier
                                    .clip(CutCornerShape(4.dp))
                                    .background(CyberSurfaceDark)
                                    .border(1.dp, if (testPromptInput == promptValue) CyberMagenta else CyberBorderLine, CutCornerShape(4.dp))
                                    .clickable { testPromptInput = promptValue }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = chipLabel,
                                    fontSize = 10.sp,
                                    color = if (testPromptInput == promptValue) CyberMagenta else CyberTextSecondary,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = testPromptInput,
                        onValueChange = { testPromptInput = it },
                        label = { Text("Prompt Instruction") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("prompt_playground_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberMagenta,
                            unfocusedBorderColor = CyberBorderLine,
                            focusedLabelColor = CyberMagenta,
                            unfocusedLabelColor = CyberTextSecondary
                        )
                    )

                    CyberButton(
                        text = if (isInferring) "RUNNING MODEL INFERENCE..." else "RUN MODEL INFERENCE",
                        onClick = {
                            viewModel.runAiInference(selectedFacilityModel, testPromptInput)
                        },
                        enabled = !isInferring,
                        color = CyberMagenta,
                        icon = Icons.Default.PlayArrow,
                        testTagStr = "run_model_inference_button"
                    )

                    if (inferenceOutput.isNotBlank()) {
                        HorizontalDivider(color = CyberBorderLine)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "INFERENCE TERMINAL STREAM:",
                                style = MaterialTheme.typography.labelMedium,
                                color = CyberTerminalGreen,
                                fontWeight = FontWeight.Bold
                            )
                            if (latencyMs > 0) {
                                Text(
                                    text = "LATENCY: ${latencyMs}ms • $speedTps TPS",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CyberYellow,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        TerminalBox(logs = inferenceOutput.split("\n"))

                        CyberButton(
                            text = if (copiedInferenceToast) "INFERENCE STREAM COPIED!" else "COPY INFERENCE OUTPUT",
                            onClick = {
                                clipboardManager.setText(AnnotatedString(inferenceOutput))
                                copiedInferenceToast = true
                            },
                            icon = Icons.Default.ContentCopy,
                            color = CyberNeonLime,
                            testTagStr = "copy_inference_output_button"
                        )
                    }
                }
            }
        }

        // Benchmark Matrix
        item {
            CyberCard(
                borderColor = CyberNeonCyan,
                title = "MODEL EVALUATION MATRIX SCOREBOARD"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    FreeAiModelCatalog.models.forEach { model ->
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = model.name,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = CyberTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${model.evaluateorScore}/100",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CyberNeonLime,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            CyberMetricBar(
                                label = "Speed Rating (${model.speedTokensPerSec} tps)",
                                score = (model.speedTokensPerSec * 100 / 250).coerceIn(10, 100),
                                color = CyberNeonCyan
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}
