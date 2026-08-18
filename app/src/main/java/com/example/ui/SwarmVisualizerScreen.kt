package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SwarmVisualizerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.swarmLogs.collectAsState()
    val isSwarmRunning by viewModel.swarmEngine.isSwarmRunning.collectAsState()
    val selectedProject by viewModel.selectedProject.collectAsState()
    val realityState by viewModel.realityState.collectAsState()

    val clipboardManager = LocalClipboardManager.current
    var copiedLogsToast by remember { mutableStateOf(false) }

    val logLines = logs.map { "${it.agentName} [${it.status}]: ${it.message}" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlitchHeader(
                title = "SWARM COROUTINE VISUALIZER",
                subtitle = "Security Guild • Performance Auditor • Arbiter Agent",
                isGlitchActive = realityState.isGlitchActive
            )
        }

        // Live Swarm Terminal Feed Box
        item {
            TerminalBox(logs = logLines)
        }

        // Action Toolbar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CyberButton(
                    text = if (isSwarmRunning) "SWARM RUNNING..." else "EXECUTE SWARM AUDIT",
                    onClick = {
                        selectedProject?.let { proj ->
                            viewModel.triggerProjectAutoUpdate(proj)
                        }
                    },
                    enabled = !isSwarmRunning && selectedProject != null,
                    color = CyberNeonCyan,
                    icon = Icons.Default.PlayArrow,
                    modifier = Modifier.weight(1f),
                    testTagStr = "execute_swarm_audit_button"
                )

                CyberIconButton(
                    icon = Icons.Default.ContentCopy,
                    onClick = {
                        val rawLogs = logs.joinToString("\n") { "[${it.agentName}] (${it.status}): ${it.message}" }
                        clipboardManager.setText(AnnotatedString(rawLogs))
                        copiedLogsToast = true
                    },
                    tint = if (copiedLogsToast) CyberNeonLime else CyberTextSecondary,
                    borderColor = if (copiedLogsToast) CyberNeonLime else CyberBorderLine,
                    contentDescription = "Copy Logs",
                    testTagStr = "copy_logs_button"
                )

                CyberIconButton(
                    icon = Icons.Default.ClearAll,
                    onClick = { viewModel.clearLogs() },
                    tint = CyberTextSecondary,
                    contentDescription = "Clear Logs",
                    testTagStr = "clear_logs_button"
                )
            }
        }

        item {
            Text(
                text = "SWARM AGENT EXECUTION LOGS",
                style = MaterialTheme.typography.titleLarge,
                color = CyberNeonLime,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        if (logs.isEmpty()) {
            item {
                CyberCard(borderColor = CyberBorderLine) {
                    Text(
                        text = "No swarm agent logs recorded yet. Tap 'Execute Swarm Audit' above.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyberTextSecondary
                    )
                }
            }
        } else {
            items(logs) { log ->
                val timeStr = SimpleDateFormat("HH:mm:ss.SSS", Locale.US).format(Date(log.timestamp))
                val statusColor = when (log.status) {
                    "APPROVED", "PASSED" -> CyberNeonLime
                    "REJECTED" -> CyberErrorRed
                    "MUTATING" -> CyberYellow
                    else -> CyberNeonCyan
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CutCornerShape(4.dp))
                        .background(CyberCardBg)
                        .border(1.dp, CyberBorderLine, CutCornerShape(4.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Memory,
                                    contentDescription = null,
                                    tint = statusColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = log.agentName,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = CyberTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = timeStr,
                                style = MaterialTheme.typography.labelMedium,
                                color = CyberTextSecondary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = log.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CyberTextSecondary,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        CyberBadge(text = log.status, color = statusColor)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}
