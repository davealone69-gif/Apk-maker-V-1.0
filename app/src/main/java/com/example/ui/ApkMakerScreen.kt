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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import com.example.domain.ApkProject
import com.example.domain.DefaultTemplates
import com.example.domain.DeployChannel
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
fun ApkMakerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.allProjects.collectAsState()
    val selectedProject by viewModel.selectedProject.collectAsState()
    val isSwarmRunning by viewModel.swarmEngine.isSwarmRunning.collectAsState()
    val realityState by viewModel.realityState.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedTemplateId by remember { mutableStateOf(DefaultTemplates.list.first().id) }
    var customNameInput by remember { mutableStateOf("") }
    var customPackageInput by remember { mutableStateOf("") }

    val exportedSource by viewModel.exportedSource.collectAsState()
    val hardwareReport by viewModel.deviceHardwareReport.collectAsState()
    val pingResult by viewModel.pingResult.collectAsState()
    val isPinging by viewModel.isPinging.collectAsState()
    val checksums by viewModel.cryptoChecksums.collectAsState()
    val generatedApk by viewModel.generatedApkArtifact.collectAsState()
    val isBuildingApk by viewModel.isBuildingApk.collectAsState()
    val apkBuildProgress by viewModel.apkBuildProgress.collectAsState()
    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current
    var selectedExportTab by remember { mutableStateOf(0) }
    var copiedToast by remember { mutableStateOf(false) }
    var pingUrlInput by remember { mutableStateOf("https://www.google.com/generate_204") }
    var checksumInput by remember { mutableStateOf("com.devator.autobuild.v100") }

    // Export Source Bundle Dialog
    if (exportedSource != null) {
        val source = exportedSource!!
        val tabs = listOf("build.gradle.kts", "AndroidManifest.xml", "MainActivity.kt", "proguard-rules.pro", "settings.gradle.kts")
        val currentCode = when (selectedExportTab) {
            0 -> source.buildGradleKts
            1 -> source.androidManifestXml
            2 -> source.mainActivityKt
            3 -> source.proguardRulesPro
            else -> source.settingsGradleKts
        }

        AlertDialog(
            onDismissRequest = { viewModel.clearExportedSource() },
            containerColor = CyberObsidian,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXPORT: ${source.projectName.uppercase()}",
                        style = MaterialTheme.typography.titleMedium,
                        color = CyberNeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                    CyberBadge(text = source.packageName, color = CyberNeonLime)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TabRow(
                        selectedTabIndex = selectedExportTab,
                        containerColor = CyberSurfaceDark,
                        contentColor = CyberNeonCyan,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedExportTab]),
                                color = CyberNeonCyan
                            )
                        }
                    ) {
                        tabs.forEachIndexed { index, tabTitle ->
                            Tab(
                                selected = selectedExportTab == index,
                                onClick = { selectedExportTab = index },
                                text = {
                                    Text(
                                        text = tabTitle,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (selectedExportTab == index) CyberNeonCyan else CyberTextSecondary
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(CutCornerShape(4.dp))
                            .background(CyberSurfaceDark)
                            .border(1.dp, CyberBorderLine, CutCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                Text(
                                    text = currentCode,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CyberTerminalGreen,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    CyberButton(
                        text = if (copiedToast) "COPIED TO CLIPBOARD!" else "COPY ${tabs[selectedExportTab].uppercase()}",
                        onClick = {
                            clipboardManager.setText(AnnotatedString(currentCode))
                            copiedToast = true
                        },
                        icon = Icons.Default.ContentCopy,
                        color = CyberNeonLime,
                        testTagStr = "copy_code_button"
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    selectedProject?.let { proj ->
                        CyberButton(
                            text = "SHARE / EXPORT ZIP ARCHIVE",
                            onClick = {
                                viewModel.shareProjectZip(context, proj)
                            },
                            icon = Icons.Default.Share,
                            color = CyberNeonCyan,
                            testTagStr = "share_zip_dialog_button"
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.clearExportedSource() },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceDark),
                    shape = CutCornerShape(4.dp)
                ) {
                    Text("CLOSE", color = CyberTextPrimary)
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlitchHeader(
                title = "APK MAKER & BUILDER",
                subtitle = "Devator Mutation Engine • Automated Update Deployer",
                isGlitchActive = realityState.isGlitchActive
            )
        }

        // Architectural Reality & Capability Audit
        item {
            CyberCard(
                borderColor = CyberNeonCyan,
                title = "SYSTEM ARCHITECTURE & CAPABILITY AUDIT",
                badgeText = "VERIFIED REAL",
                badgeColor = CyberNeonLime
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "1. Local SQLite Room Database: Real on-device persistence for all projects, mutation changelogs, and audit logs.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyberTextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "2. Swarm State Machine: Real concurrent Coroutines verifying Target SDK, R8 optimization, and ProGuard rules.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyberTextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "3. Interactive Client Simulators: Live functional Kotlin Compose UI tools (Forms, Checklists, QR Scanners, Shift Timers).",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyberTextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "4. Project Code Exporter: Generates complete build.gradle.kts, AndroidManifest.xml, and Compose code ready for APK compilation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyberTextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Active Project Selector Banner
        item {
            CyberCard(
                borderColor = CyberNeonCyan,
                title = "ACTIVE PROJECT SELECTOR"
            ) {
                if (projects.isEmpty()) {
                    Text(
                        text = "No projects created. Choose a Work Time-Saving template below.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyberTextSecondary
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    ) {
                        items(projects) { proj ->
                            val isSelected = proj.id == selectedProject?.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(CutCornerShape(4.dp))
                                    .background(if (isSelected) CyberSurfaceDark else CyberObsidian)
                                    .border(
                                        1.dp,
                                        if (isSelected) CyberNeonLime else CyberBorderLine,
                                        CutCornerShape(4.dp)
                                    )
                                    .clickable { viewModel.selectProject(proj) }
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Android,
                                        contentDescription = null,
                                        tint = if (isSelected) CyberNeonLime else CyberTextSecondary,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Column {
                                        Text(
                                            text = proj.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = CyberTextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${proj.packageName} • v${proj.versionName} (${proj.versionCode})",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = CyberTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                CyberBadge(
                                    text = proj.deployChannel.name,
                                    color = if (isSelected) CyberNeonCyan else CyberTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Project Quick Config Panel
        selectedProject?.let { proj ->
            item {
                CyberCard(
                    borderColor = CyberNeonLime,
                    title = "PROJECT CONFIG & AUTO-UPDATE SETTINGS",
                    badgeText = "v${proj.versionName}",
                    badgeColor = CyberNeonLime
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Target SDK: ${proj.targetSdk} • Patches Deployed: ${proj.activePatchCount}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CyberTextSecondary,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "Auto-Deploy Pipeline: ${if (proj.isAutoDeployEnabled) "ENABLED" else "PAUSED"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (proj.isAutoDeployEnabled) CyberNeonLime else CyberYellow,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            CyberIconButton(
                                icon = Icons.Default.Delete,
                                onClick = { viewModel.deleteProject(proj.id) },
                                tint = CyberErrorRed,
                                contentDescription = "Delete Project",
                                testTagStr = "delete_project_button"
                            )
                        }

                        HorizontalDivider(color = CyberBorderLine)

                        // Toggles for ProGuard & R8
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = CyberNeonCyan,
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                                Text(
                                    text = "R8 & ProGuard Shrinking",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CyberTextPrimary
                                )
                            }
                            Switch(
                                checked = proj.isProGuardActive,
                                onCheckedChange = { checked ->
                                    viewModel.updateProjectSettings(proj.copy(isProGuardActive = checked, isR8ShrinkingEnabled = checked))
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CyberObsidian,
                                    checkedTrackColor = CyberNeonCyan
                                )
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = null,
                                    tint = CyberYellow,
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                                Text(
                                    text = "Auto-Deploy On Mutation",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CyberTextPrimary
                                )
                            }
                            Switch(
                                checked = proj.isAutoDeployEnabled,
                                onCheckedChange = { checked ->
                                    viewModel.updateProjectSettings(proj.copy(isAutoDeployEnabled = checked))
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CyberObsidian,
                                    checkedTrackColor = CyberNeonLime
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        CyberButton(
                            text = if (isSwarmRunning) "MUTATION IN PROGRESS..." else "DRAFT MUTATION & AUTO-DEPLOY PATCH",
                            onClick = {
                                viewModel.triggerProjectAutoUpdate(proj)
                            },
                            enabled = !isSwarmRunning,
                            color = CyberNeonLime,
                            icon = Icons.Default.AutoAwesome,
                            testTagStr = "trigger_mutation_button"
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        CyberButton(
                            text = "EXPORT SOURCE BUNDLE & GRADLE CONFIG",
                            onClick = {
                                viewModel.generateProjectExportSource(proj)
                            },
                            color = CyberNeonCyan,
                            icon = Icons.Default.Code,
                            testTagStr = "export_project_code_button"
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        CyberButton(
                            text = "EXPORT & SHARE ZIP ARCHIVE (.ZIP)",
                            onClick = {
                                viewModel.shareProjectZip(context, proj)
                            },
                            color = CyberTerminalGreen,
                            icon = Icons.Default.Share,
                            testTagStr = "share_project_zip_button"
                        )
                    }
                }
            }
        }

        // REAL ON-DEVICE LOCAL APK COMPILER & INSTALLER
        selectedProject?.let { proj ->
            item {
                CyberCard(
                    borderColor = CyberNeonLime,
                    title = "LOCAL ON-DEVICE SIGNED APK BUILDER & INSTALLER",
                    badgeText = if (isBuildingApk) "BUILDING..." else if (generatedApk != null) "APK READY" else "STANDALONE",
                    badgeColor = if (isBuildingApk) CyberYellow else if (generatedApk != null) CyberNeonLime else CyberNeonCyan
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Synthesizes, packages, and signs an installable Android APK directly on this device using local Dalvik/DEX assembly and on-device RSA 2048-bit keypair signing.",
                            style = MaterialTheme.typography.bodySmall,
                            color = CyberTextSecondary
                        )

                        CyberButton(
                            text = if (isBuildingApk) "COMPILING & SIGNING ON DEVICE..." else "BUILD SIGNED APK ON THIS DEVICE (.APK)",
                            onClick = {
                                viewModel.buildSignedApkLocally(context, proj)
                            },
                            enabled = !isBuildingApk,
                            color = CyberNeonLime,
                            icon = Icons.Default.Build,
                            testTagStr = "build_signed_apk_locally_button"
                        )

                        apkBuildProgress?.let { progress ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberSurfaceDark)
                                    .border(1.dp, CyberBorderLine, RoundedCornerShape(4.dp))
                                    .padding(8.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "STAGE: ${progress.stage}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CyberNeonCyan,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "${progress.progressPercent}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = CyberNeonLime,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    Text(
                                        text = progress.logMessage,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CyberTextPrimary,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        generatedApk?.let { apk ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberSurfaceDark)
                                    .border(1.dp, CyberNeonLime, RoundedCornerShape(4.dp))
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Android,
                                                contentDescription = null,
                                                tint = CyberNeonLime,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = apk.fileName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = CyberNeonLime,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }
                                        CyberBadge(
                                            text = apk.fileSizeFormatted,
                                            color = CyberNeonCyan
                                        )
                                    }

                                    HorizontalDivider(color = CyberBorderLine)

                                    Text(
                                        text = "PACKAGE: ${apk.packageName} (v${apk.versionName} code ${apk.versionCode})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CyberTextSecondary,
                                        fontFamily = FontFamily.Monospace
                                    )

                                    Text(
                                        text = "SHA-256: ${apk.sha256Checksum}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CyberTextPrimary,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )

                                    Text(
                                        text = "MD5: ${apk.md5Checksum}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CyberTextSecondary,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        CyberButton(
                                            text = "INSTALL APK ON THIS PHONE",
                                            onClick = {
                                                viewModel.installGeneratedApk(context)
                                            },
                                            color = CyberNeonLime,
                                            icon = Icons.Default.Android,
                                            modifier = Modifier.weight(1f),
                                            testTagStr = "install_generated_apk_button"
                                        )

                                        CyberButton(
                                            text = "SHARE .APK",
                                            onClick = {
                                                viewModel.shareGeneratedApk(context)
                                            },
                                            color = CyberNeonCyan,
                                            icon = Icons.Default.Share,
                                            modifier = Modifier.width(130.dp),
                                            testTagStr = "share_generated_apk_button"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // REAL HARDWARE & SYSTEM TELEMETRY
        hardwareReport?.let { hw ->
            item {
                CyberCard(
                    borderColor = CyberNeonLime,
                    title = "REAL ANDROID HARDWARE & RUNTIME TELEMETRY",
                    badgeText = "PHYSICAL PROBE",
                    badgeColor = CyberNeonLime
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "DEVICE: ${hw.brand} ${hw.model}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CyberNeonCyan,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "OS: Android ${hw.androidVersion} (API Level ${hw.apiLevel})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CyberTextSecondary
                                )
                                Text(
                                    text = "CHIPSET: ${hw.hardwareChipset} | BOARD: ${hw.board}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CyberTextSecondary
                                )
                            }
                            CyberIconButton(
                                icon = Icons.Default.Refresh,
                                onClick = { viewModel.refreshHardwareDiagnostics() },
                                tint = CyberNeonLime,
                                contentDescription = "Refresh Telemetry",
                                testTagStr = "refresh_telemetry_button"
                            )
                        }

                        HorizontalDivider(color = CyberBorderLine)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "PHYSICAL RAM",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberTextSecondary
                                )
                                Text(
                                    text = "${hw.availRamMb} MB free / ${hw.totalRamMb} MB",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CyberTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Column {
                                Text(
                                    text = "INTERNAL STORAGE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberTextSecondary
                                )
                                Text(
                                    text = "${hw.freeStorageGb} GB free / ${hw.totalStorageGb} GB",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CyberTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "BATTERY & POWER",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberTextSecondary
                                )
                                Text(
                                    text = "${hw.batteryPct}% ${if (hw.isBatteryCharging) "(CHARGING)" else "(DISCHARGING)"} • ${hw.batteryTemperatureC}°C",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (hw.batteryPct < 20) CyberErrorRed else CyberTerminalGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Column {
                                Text(
                                    text = "NETWORK LINK",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberTextSecondary
                                )
                                Text(
                                    text = "${hw.networkType} (${hw.linkDownstreamKbps} Kbps)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (hw.isNetworkConnected) CyberNeonLime else CyberErrorRed,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Text(
                            text = "CPU ABIs: ${hw.supportedAbis.joinToString(", ")} | SCREEN: ${hw.screenResolution} (${hw.displayDensityDpi} dpi)",
                            style = MaterialTheme.typography.bodySmall,
                            color = CyberTextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // REAL HTTP NETWORK PING & DIAGNOSTIC TOOL
        item {
            CyberCard(
                borderColor = CyberNeonCyan,
                title = "LIVE HTTP NETWORK PROBE & LATENCY TESTER",
                badgeText = if (isPinging) "PINGING..." else "READY",
                badgeColor = if (isPinging) CyberYellow else CyberNeonCyan
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Execute live HTTP probe requests over active Android network connection to test server latency and response code.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyberTextSecondary
                    )

                    OutlinedTextField(
                        value = pingUrlInput,
                        onValueChange = { pingUrlInput = it },
                        label = { Text("Probe Target URL") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ping_url_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberNeonCyan,
                            unfocusedBorderColor = CyberBorderLine,
                            focusedLabelColor = CyberNeonCyan,
                            unfocusedLabelColor = CyberTextSecondary
                        )
                    )

                    CyberButton(
                        text = if (isPinging) "EXECUTING HTTP PROBE..." else "EXECUTE LIVE HTTP PING",
                        onClick = { viewModel.executeNetworkPing(pingUrlInput) },
                        enabled = !isPinging,
                        icon = Icons.Default.Language,
                        color = CyberNeonCyan,
                        testTagStr = "execute_ping_button"
                    )

                    pingResult?.let { ping ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (ping.isSuccess) CyberSurfaceDark else CyberErrorRed.copy(alpha = 0.15f))
                                .border(1.dp, if (ping.isSuccess) CyberNeonLime else CyberErrorRed, RoundedCornerShape(4.dp))
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "TARGET: ${ping.url}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyberTextSecondary,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "STATUS: ${ping.message} • PROTOCOL: ${ping.protocol}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (ping.isSuccess) CyberNeonLime else CyberErrorRed,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        // REAL CRYPTOGRAPHIC HASHER
        item {
            CyberCard(
                borderColor = CyberMagenta,
                title = "LIVE CRYPTOGRAPHIC HASH & CHECKSUM ENGINE",
                badgeText = "SHA-256 / MD5 / SHA-1",
                badgeColor = CyberMagenta
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Calculate cryptographic verification hashes using Java Security MessageDigest on active Android runtime.",
                        style = MaterialTheme.typography.bodySmall,
                        color = CyberTextSecondary
                    )

                    OutlinedTextField(
                        value = checksumInput,
                        onValueChange = {
                            checksumInput = it
                            viewModel.computeStringChecksums(it)
                        },
                        label = { Text("Input String / Manifest Digest") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checksum_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberMagenta,
                            unfocusedBorderColor = CyberBorderLine,
                            focusedLabelColor = CyberMagenta,
                            unfocusedLabelColor = CyberTextSecondary
                        )
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        checksums.forEach { hashItem ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyberSurfaceDark)
                                    .border(1.dp, CyberBorderLine, RoundedCornerShape(4.dp))
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "${hashItem.algorithm} (${hashItem.inputBytesLength} bytes):",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CyberMagenta,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = hashItem.hexHash,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = CyberTextPrimary,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Work Time-Saving Tool Templates Catalog
        item {
            Text(
                text = "WORK TIME-SAVING TOOL PRESETS",
                style = MaterialTheme.typography.titleLarge,
                color = CyberNeonCyan,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        items(DefaultTemplates.list) { template ->
            CyberCard(
                borderColor = CyberBorderLine,
                title = template.name,
                badgeText = template.category.name,
                badgeColor = CyberYellow
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = template.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyberTextSecondary
                    )

                    Column {
                        template.featureHighlights.forEach { feature ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = CyberTerminalGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = feature,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CyberTextPrimary,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    CyberButton(
                        text = "BUILD & AUTO-DEPLOY APK",
                        onClick = {
                            viewModel.createProjectFromTemplate(
                                templateId = template.id,
                                customName = template.name,
                                packageName = template.defaultPackageName,
                                channel = DeployChannel.PRODUCTION
                            )
                        },
                        icon = Icons.Default.Add,
                        color = CyberNeonCyan,
                        testTagStr = "build_template_${template.id}_button"
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}
