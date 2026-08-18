package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.ApkProject
import com.example.mandelacore.MandelaCore
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
fun ClientDevicePreviewScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val selectedProject by viewModel.selectedProject.collectAsState()
    val realityState by viewModel.realityState.collectAsState()
    val hardwareReport by viewModel.deviceHardwareReport.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlitchHeader(
                title = "LIVE CLIENT DEVICE SIMULATOR",
                subtitle = "Over-The-Air Update Receiver • Interactive Work Tool",
                isGlitchActive = realityState.isGlitchActive
            )
        }

        // OTA Update Live Toast / Notification Bar
        item {
            AnimatedVisibility(
                visible = realityState.activePatchNotification != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CutCornerShape(6.dp))
                        .background(CyberNeonLime)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = null,
                                tint = CyberObsidian,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = realityState.activePatchNotification ?: "",
                                style = MaterialTheme.typography.labelLarge,
                                color = CyberObsidian,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        IconButton(
                            onClick = { MandelaCore.clearNotification() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = CyberObsidian
                            )
                        }
                    }
                }
            }
        }

        // REAL HARDWARE-AWARE MOBILE DEVICE FRAME
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp))
                    .background(CyberObsidian)
                    .border(2.dp, CyberNeonCyan, CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Mobile Device Status Bar with Real Hardware Telemetry
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                tint = CyberNeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = hardwareReport?.let { "${it.brand} ${it.model}" } ?: "CLIENT DEVICE #802",
                                style = MaterialTheme.typography.labelMedium,
                                color = CyberTextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            hardwareReport?.let { hw ->
                                CyberBadge(
                                    text = "${hw.batteryPct}% BAT",
                                    color = if (hw.batteryPct < 20) CyberErrorRed else CyberTerminalGreen
                                )
                                CyberBadge(
                                    text = hw.networkType,
                                    color = CyberNeonCyan
                                )
                            }
                            CyberBadge(
                                text = "v${selectedProject?.versionName ?: "1.0.0"}",
                                color = CyberNeonLime
                            )
                        }
                    }

                    HorizontalDivider(color = CyberBorderLine)

                    // Interactive Tool UI preview based on selected template
                    val templateId = selectedProject?.templateId ?: "auto_form"

                    when (templateId) {
                        "auto_form" -> AutoFormToolInteractive()
                        "daily_audit" -> DailyAuditToolInteractive()
                        "inventory_scanner" -> InventoryScannerInteractive()
                        "time_tracker" -> ShiftTimerToolInteractive()
                        else -> AutoFormToolInteractive()
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun AutoFormToolInteractive() {
    var fieldName by remember { mutableStateOf("Site Inspection - Zone B") }
    var technicianName by remember { mutableStateOf("Alex Mercer") }
    var notes by remember { mutableStateOf("Equipment calibrated. Zero safety violations.") }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CyberSurfaceDark)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "WORK TOOL: AUTO-FORM DISPATCHER",
                style = MaterialTheme.typography.titleMedium,
                color = CyberNeonCyan,
                fontWeight = FontWeight.Bold
            )
            CyberBadge(text = "LIVE APK DEMO", color = CyberYellow)
        }

        OutlinedTextField(
            value = fieldName,
            onValueChange = { fieldName = it },
            label = { Text("Task / Form Title") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("form_title_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberNeonCyan,
                unfocusedBorderColor = CyberBorderLine,
                focusedLabelColor = CyberNeonCyan,
                unfocusedLabelColor = CyberTextSecondary
            )
        )

        OutlinedTextField(
            value = technicianName,
            onValueChange = { technicianName = it },
            label = { Text("Technician / Employee Name") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("form_employee_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberNeonCyan,
                unfocusedBorderColor = CyberBorderLine,
                focusedLabelColor = CyberNeonCyan,
                unfocusedLabelColor = CyberTextSecondary
            )
        )

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Field Notes & Audit Summary") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("form_notes_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberNeonCyan,
                unfocusedBorderColor = CyberBorderLine,
                focusedLabelColor = CyberNeonCyan,
                unfocusedLabelColor = CyberTextSecondary
            )
        )

        if (submitted) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberNeonLime.copy(alpha = 0.2f))
                    .border(1.dp, CyberNeonLime, RoundedCornerShape(4.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FORM SUBMITTED TO CLOUD! GPS & Time Stamped.",
                    style = MaterialTheme.typography.labelLarge,
                    color = CyberNeonLime,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        CyberButton(
            text = if (submitted) "DISPATCH ANOTHER FORM" else "AUTO-SUBMIT FIELD REPORT",
            onClick = { submitted = !submitted },
            color = CyberNeonLime,
            icon = Icons.Default.Send,
            testTagStr = "submit_field_form_button"
        )
    }
}

@Composable
fun DailyAuditToolInteractive() {
    var check1 by remember { mutableStateOf(true) }
    var check2 by remember { mutableStateOf(true) }
    var check3 by remember { mutableStateOf(false) }
    var auditSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CyberSurfaceDark)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "WORK TOOL: DAILY AUDIT CHECKLIST",
                style = MaterialTheme.typography.titleMedium,
                color = CyberNeonLime,
                fontWeight = FontWeight.Bold
            )
            CyberBadge(
                text = if (auditSubmitted) "AUDIT COMPLIANT" else "PENDING SIGN-OFF",
                color = if (auditSubmitted) CyberNeonLime else CyberYellow
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { check1 = !check1 }
        ) {
            Checkbox(
                checked = check1,
                onCheckedChange = { check1 = it },
                colors = CheckboxDefaults.colors(checkedColor = CyberNeonLime)
            )
            Text("Safety protocols verified & PPE equipped", color = CyberTextPrimary)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { check2 = !check2 }
        ) {
            Checkbox(
                checked = check2,
                onCheckedChange = { check2 = it },
                colors = CheckboxDefaults.colors(checkedColor = CyberNeonLime)
            )
            Text("Tool inventory & battery charge checked", color = CyberTextPrimary)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { check3 = !check3 }
        ) {
            Checkbox(
                checked = check3,
                onCheckedChange = { check3 = it },
                colors = CheckboxDefaults.colors(checkedColor = CyberNeonLime)
            )
            Text("Manager sign-off log dispatched", color = CyberTextPrimary)
        }

        if (auditSubmitted) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberNeonLime.copy(alpha = 0.2f))
                    .border(1.dp, CyberNeonLime, RoundedCornerShape(4.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AUDIT LOGGED & SYNCHRONIZED ACROSS WORK GROUP",
                    style = MaterialTheme.typography.labelMedium,
                    color = CyberNeonLime,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CyberButton(
                text = if (auditSubmitted) "UPDATE SIGN-OFF" else "LOG COMPLIANCE AUDIT",
                onClick = {
                    check1 = true
                    check2 = true
                    check3 = true
                    auditSubmitted = true
                },
                color = CyberNeonCyan,
                icon = Icons.Default.Assignment,
                modifier = Modifier.weight(1f),
                testTagStr = "log_daily_compliance_button"
            )

            if (auditSubmitted) {
                CyberButton(
                    text = "RESET",
                    onClick = {
                        auditSubmitted = false
                        check3 = false
                    },
                    color = CyberBorderLine,
                    icon = Icons.Default.Refresh,
                    modifier = Modifier.width(100.dp),
                    testTagStr = "reset_compliance_button"
                )
            }
        }
    }
}

@Composable
fun InventoryScannerInteractive() {
    var stockCount by remember { mutableIntStateOf(142) }
    var lastScannedBarcode by remember { mutableStateOf("BAR-882910-X") }
    var manualBarcodeEntry by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CyberSurfaceDark)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "WORK TOOL: QR INVENTORY AUDITOR",
                style = MaterialTheme.typography.titleMedium,
                color = CyberYellow,
                fontWeight = FontWeight.Bold
            )
            CyberBadge(text = "SCANNER READY", color = CyberNeonLime)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Last Scanned Barcode:", style = MaterialTheme.typography.labelMedium, color = CyberTextSecondary)
                Text(lastScannedBarcode, style = MaterialTheme.typography.titleLarge, color = CyberTextPrimary, fontFamily = FontFamily.Monospace)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Stock Count:", style = MaterialTheme.typography.labelMedium, color = CyberTextSecondary)
                Text("$stockCount units", style = MaterialTheme.typography.titleLarge, color = CyberTerminalGreen, fontFamily = FontFamily.Monospace)
            }
        }

        OutlinedTextField(
            value = manualBarcodeEntry,
            onValueChange = { manualBarcodeEntry = it },
            label = { Text("Manual Serial / SKU Entry") },
            placeholder = { Text("SKU-9921-A...") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("manual_sku_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CyberYellow,
                unfocusedBorderColor = CyberBorderLine,
                focusedLabelColor = CyberYellow,
                unfocusedLabelColor = CyberTextSecondary
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CyberButton(
                text = if (manualBarcodeEntry.isNotBlank()) "+1 LOG SKU" else "+1 SCAN ITEM",
                onClick = {
                    stockCount += 1
                    if (manualBarcodeEntry.isNotBlank()) {
                        lastScannedBarcode = manualBarcodeEntry.trim().uppercase()
                        manualBarcodeEntry = ""
                    } else {
                        lastScannedBarcode = "BAR-${(100000..999999).random()}-X"
                    }
                },
                color = CyberYellow,
                icon = Icons.Default.QrCodeScanner,
                modifier = Modifier.weight(1f),
                testTagStr = "scan_inventory_item_button"
            )

            CyberButton(
                text = "CLEAR",
                onClick = {
                    stockCount = 0
                    lastScannedBarcode = "EMPTY_BIN"
                },
                color = CyberBorderLine,
                icon = Icons.Default.Refresh,
                modifier = Modifier.width(95.dp),
                testTagStr = "clear_inventory_button"
            )
        }
    }
}

@Composable
fun ShiftTimerToolInteractive() {
    var isClockedIn by remember { mutableStateOf(false) }
    var shiftSeconds by remember { mutableLongStateOf(28400L) } // ~7.8 hours

    androidx.compose.runtime.LaunchedEffect(isClockedIn) {
        while (isClockedIn) {
            kotlinx.coroutines.delay(1000L)
            shiftSeconds += 1L
        }
    }

    val hours = shiftSeconds / 3600
    val minutes = (shiftSeconds % 3600) / 60
    val seconds = shiftSeconds % 60

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CyberSurfaceDark)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "WORK TOOL: SHIFT & TASK TIMER",
                style = MaterialTheme.typography.titleMedium,
                color = CyberMagenta,
                fontWeight = FontWeight.Bold
            )
            CyberBadge(
                text = if (isClockedIn) "LIVE TICKING..." else "OFF SHIFT",
                color = if (isClockedIn) CyberNeonLime else CyberTextSecondary
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("REAL-TIME DURATION:", style = MaterialTheme.typography.labelMedium, color = CyberTextSecondary)
                Text(
                    text = String.format("%02dh %02dm %02ds", hours, minutes, seconds),
                    style = MaterialTheme.typography.headlineMedium,
                    color = CyberNeonCyan,
                    fontFamily = FontFamily.Monospace
                )
            }
            CyberBadge(text = "BREAKS AUTO-DEDUCTED", color = CyberYellow)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CyberButton(
                text = if (isClockedIn) "CLOCK OUT" else "CLOCK IN FOR SHIFT",
                onClick = { isClockedIn = !isClockedIn },
                color = if (isClockedIn) CyberErrorRed else CyberMagenta,
                icon = Icons.Default.Timer,
                modifier = Modifier.weight(1f),
                testTagStr = "clock_in_out_button"
            )
            CyberButton(
                text = "RESET",
                onClick = {
                    isClockedIn = false
                    shiftSeconds = 0L
                },
                color = CyberBorderLine,
                icon = Icons.Default.Refresh,
                modifier = Modifier.width(100.dp),
                testTagStr = "reset_timer_button"
            )
        }
    }
}
