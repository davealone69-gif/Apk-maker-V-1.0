package com.example.ui

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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.AutoUpdateTrigger
import com.example.domain.DeployChannel
import com.example.ui.theme.CyberBorderLine
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
fun AutoDeployPipelineScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedProject by viewModel.selectedProject.collectAsState()
    val mutations by viewModel.allMutations.collectAsState()
    val isSwarmRunning by viewModel.swarmEngine.isSwarmRunning.collectAsState()
    val realityState by viewModel.realityState.collectAsState()

    var customPatchNote by remember { mutableStateOf("Security hardening patch: R8 optimization pass & ProGuard rule refresh") }

    val latestMutation = mutations.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlitchHeader(
                title = "AUTO-DEPLOY PIPELINE",
                subtitle = "Over-The-Air Update Engine • Consensus Approval Gateway",
                isGlitchActive = realityState.isGlitchActive
            )
        }

        // Live Channel Status
        selectedProject?.let { proj ->
            item {
                CyberCard(
                    borderColor = CyberNeonCyan,
                    title = "DEPLOYMENT CHANNEL SELECTOR",
                    badgeText = proj.deployChannel.name,
                    badgeColor = when (proj.deployChannel) {
                        DeployChannel.PRODUCTION -> CyberNeonLime
                        DeployChannel.STAGING -> CyberYellow
                        DeployChannel.CANARY -> CyberMagenta
                    }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Select active deployment target channel for project '${proj.name}':",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CyberTextSecondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DeployChannel.entries.forEach { ch ->
                                val isSelected = proj.deployChannel == ch
                                val col = when (ch) {
                                    DeployChannel.PRODUCTION -> CyberNeonLime
                                    DeployChannel.STAGING -> CyberYellow
                                    DeployChannel.CANARY -> CyberMagenta
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clip(CutCornerShape(4.dp))
                                        .background(if (isSelected) col.copy(alpha = 0.2f) else CyberObsidian)
                                        .border(
                                            if (isSelected) 2.dp else 1.dp,
                                            if (isSelected) col else CyberBorderLine,
                                            CutCornerShape(4.dp)
                                        )
                                        .clickable {
                                            viewModel.updateProjectSettings(proj.copy(deployChannel = ch))
                                        }
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ch.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) col else CyberTextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = CyberBorderLine)

                        // Trigger Option
                        Text(
                            text = "AUTO-UPDATE TRIGGER STRATEGY:",
                            style = MaterialTheme.typography.labelMedium,
                            color = CyberNeonCyan
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AutoUpdateTrigger.entries.forEach { trig ->
                                val isSelected = proj.updateTrigger == trig
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(CutCornerShape(4.dp))
                                        .background(if (isSelected) CyberSurfaceDark else CyberObsidian)
                                        .border(
                                            1.dp,
                                            if (isSelected) CyberNeonCyan else CyberBorderLine,
                                            CutCornerShape(4.dp)
                                        )
                                        .clickable {
                                            viewModel.updateProjectSettings(proj.copy(updateTrigger = trig))
                                        }
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = trig.name.replace("_", "\n"),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) CyberNeonCyan else CyberTextSecondary,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // EVALUATEOR CONSENSUS RADAR
            item {
                CyberCard(
                    borderColor = CyberNeonLime,
                    title = "EVALUATEOR CONSENSUS RADAR",
                    badgeText = if (latestMutation != null) "${latestMutation.evaluateorScore}/100" else "READY",
                    badgeColor = CyberNeonLime
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Every automated update is scored across Security, Performance, and UX impact before consensus approval is granted.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CyberTextSecondary
                        )

                        CyberMetricBar(
                            label = "Security Guild Score",
                            score = latestMutation?.securityScore ?: 96,
                            color = CyberNeonCyan
                        )

                        CyberMetricBar(
                            label = "Performance Auditor Score",
                            score = latestMutation?.performanceScore ?: 94,
                            color = CyberNeonLime
                        )

                        CyberMetricBar(
                            label = "UX Impact Score",
                            score = latestMutation?.uxScore ?: 92,
                            color = CyberMagenta
                        )

                        HorizontalDivider(color = CyberBorderLine)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "CONSENSUS STATUS:",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CyberTextSecondary
                                )
                                Text(
                                    text = if (latestMutation?.consensusApproved == true || latestMutation == null) "STABILITY VERIFIED • APPROVED" else "REJECTED BY ARBITER",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (latestMutation?.consensusApproved == true || latestMutation == null) CyberTerminalGreen else CyberErrorRed,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            CyberBadge(
                                text = "THRESHOLD: 75",
                                color = CyberYellow
                            )
                        }
                    }
                }
            }

            // OTA Patch Broadcast Dispatcher
            item {
                CyberCard(
                    borderColor = CyberYellow,
                    title = "OTA PATCH BROADCASTER DISPATCH"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = CyberYellow,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Instant Over-The-Air Patch Trigger",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = CyberTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Compiles hot-patch bytecode and broadcasts live update to connected device pool.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CyberTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        OutlinedTextField(
                            value = customPatchNote,
                            onValueChange = { customPatchNote = it },
                            label = { Text("Hotfix Mutation Changelog Note") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("hotfix_changelog_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberYellow,
                                unfocusedBorderColor = CyberBorderLine,
                                focusedLabelColor = CyberYellow,
                                unfocusedLabelColor = CyberTextSecondary
                            )
                        )

                        CyberButton(
                            text = if (isSwarmRunning) "DISPATCHING OTA UPDATE..." else "FORCE DISPATCH OTA UPDATE NOW",
                            onClick = {
                                viewModel.triggerProjectAutoUpdate(proj, customPatchNote)
                            },
                            enabled = !isSwarmRunning,
                            color = CyberYellow,
                            icon = Icons.Default.FlashOn,
                            testTagStr = "force_ota_dispatch_button"
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}
