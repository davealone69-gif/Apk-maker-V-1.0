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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
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
fun MutationLogsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val mutations by viewModel.allMutations.collectAsState()
    val realityState by viewModel.realityState.collectAsState()

    val clipboardManager = LocalClipboardManager.current
    var copiedLedgerToast by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredMutations = mutations.filter { mut ->
        val matchesCategory = when (selectedCategoryFilter) {
            "ALL" -> true
            "APPROVED" -> mut.consensusApproved
            "REJECTED" -> !mut.consensusApproved
            else -> mut.mutationType.equals(selectedCategoryFilter, ignoreCase = true)
        }
        val matchesQuery = if (searchQuery.isBlank()) true else {
            mut.title.contains(searchQuery, ignoreCase = true) ||
            mut.changelog.contains(searchQuery, ignoreCase = true) ||
            mut.mutationType.contains(searchQuery, ignoreCase = true)
        }
        matchesCategory && matchesQuery
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlitchHeader(
                title = "MUTATION AUDIT HISTORY",
                subtitle = "Evaluateor Scoring Log • Consensus Approval Records",
                isGlitchActive = realityState.isGlitchActive
            )
        }

        // Search & Filters Card
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Audit Logs & Changelogs") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = CyberNeonCyan)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mutation_search_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberNeonCyan,
                        unfocusedBorderColor = CyberBorderLine,
                        focusedLabelColor = CyberNeonCyan,
                        unfocusedLabelColor = CyberTextSecondary
                    )
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val filterOptions = listOf("ALL", "APPROVED", "REJECTED", "SECURITY_HOTFIX", "ARCHITECTURE_OPTIMIZATION", "PERFORMANCE_TUNING")
                    items(filterOptions) { filter ->
                        val isSelected = selectedCategoryFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(CutCornerShape(4.dp))
                                .background(if (isSelected) CyberSurfaceDark else CyberObsidian)
                                .border(
                                    1.dp,
                                    if (isSelected) CyberNeonCyan else CyberBorderLine,
                                    CutCornerShape(4.dp)
                                )
                                .clickable { selectedCategoryFilter = filter }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = filter.replace('_', ' '),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) CyberNeonCyan else CyberTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }

        if (mutations.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CyberButton(
                        text = if (copiedLedgerToast) "AUDIT LEDGER COPIED!" else "COPY RAW AUDIT LEDGER",
                        onClick = {
                            val rawLedger = filteredMutations.joinToString("\n\n---\n\n") { mut ->
                                val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(mut.appliedTimestamp))
                                "MUTATION ID: ${mut.id}\nTITLE: ${mut.title}\nTYPE: ${mut.mutationType}\nTIMESTAMP: $dateStr\nEVALUATOR SCORE: ${mut.evaluateorScore}/100\nSECURITY: ${mut.securityScore}% | PERF: ${mut.performanceScore}% | UX: ${mut.uxScore}%\nSTATUS: ${if (mut.consensusApproved) "CONSENSUS_PASSED" else "REJECTED"}\nCHANGELOG:\n${mut.changelog}"
                            }
                            clipboardManager.setText(AnnotatedString(rawLedger))
                            copiedLedgerToast = true
                        },
                        icon = Icons.Default.ContentCopy,
                        color = CyberNeonLime,
                        modifier = Modifier.weight(1f),
                        testTagStr = "copy_raw_ledger_button"
                    )
                }
            }
        }

        if (filteredMutations.isEmpty()) {
            item {
                CyberCard(borderColor = CyberBorderLine) {
                    Text(
                        text = if (mutations.isEmpty()) "No mutations recorded yet. Run an auto-deploy update pipeline from the APK Maker tab." else "No mutation logs match the active filter criteria.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyberTextSecondary
                    )
                }
            }
        } else {
            items(filteredMutations) { mut ->
                val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(mut.appliedTimestamp))

                CyberCard(
                    borderColor = if (mut.consensusApproved) CyberNeonLime else CyberErrorRed,
                    title = mut.title,
                    badgeText = "SCORE ${mut.evaluateorScore}/100",
                    badgeColor = if (mut.consensusApproved) CyberNeonLime else CyberErrorRed
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Type: ${mut.mutationType} • $dateStr",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CyberTextSecondary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                            CyberBadge(
                                text = if (mut.consensusApproved) "CONSENSUS PASSED" else "REJECTED",
                                color = if (mut.consensusApproved) CyberNeonCyan else CyberErrorRed
                            )
                        }

                        HorizontalDivider(color = CyberBorderLine)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Security: ${mut.securityScore}%", style = MaterialTheme.typography.labelMedium, color = CyberNeonCyan)
                            Text("Performance: ${mut.performanceScore}%", style = MaterialTheme.typography.labelMedium, color = CyberNeonLime)
                            Text("UX Impact: ${mut.uxScore}%", style = MaterialTheme.typography.labelMedium, color = CyberMagenta)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CutCornerShape(4.dp))
                                .background(CyberObsidian)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = mut.changelog,
                                style = MaterialTheme.typography.bodyMedium,
                                color = CyberTextPrimary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}
