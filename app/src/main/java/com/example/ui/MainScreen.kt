package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBorderLine
import com.example.ui.theme.CyberMagenta
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonLime
import com.example.ui.theme.CyberObsidian
import com.example.ui.theme.CyberSurfaceDark
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.CyberYellow

data class NavTabItem(
    val title: String,
    val icon: ImageVector,
    val testTagStr: String
)

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val activeTab by viewModel.activeTab.collectAsState()

    val tabs = listOf(
        NavTabItem("MAKER", Icons.Default.Build, "tab_maker"),
        NavTabItem("PIPELINE", Icons.Default.CloudDone, "tab_pipeline"),
        NavTabItem("SWARM", Icons.Default.Memory, "tab_swarm"),
        NavTabItem("AI LAB", Icons.Default.AutoAwesome, "tab_ai_lab"),
        NavTabItem("DEVICE", Icons.Default.PhoneAndroid, "tab_device"),
        NavTabItem("AUDIT", Icons.Default.History, "tab_audit")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CyberObsidian,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberSurfaceDark)
                    .border(androidx.compose.foundation.BorderStroke(1.dp, CyberBorderLine))
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    tabs.forEachIndexed { index, item ->
                        val isSelected = activeTab == index
                        val color = when (index) {
                            0 -> CyberNeonCyan
                            1 -> CyberNeonLime
                            2 -> CyberYellow
                            3 -> CyberMagenta
                            4 -> CyberNeonLime
                            else -> CyberNeonCyan
                        }

                        Column(
                            modifier = Modifier
                                .testTag(item.testTagStr)
                                .clip(CutCornerShape(4.dp))
                                .background(if (isSelected) color.copy(alpha = 0.2f) else CyberObsidian)
                                .border(
                                    1.dp,
                                    if (isSelected) color else CyberBorderLine,
                                    CutCornerShape(4.dp)
                                )
                                .clickable { viewModel.selectTab(index) }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (isSelected) color else CyberTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSelected) color else CyberTextSecondary,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                0 -> ApkMakerScreen(viewModel = viewModel)
                1 -> AutoDeployPipelineScreen(viewModel = viewModel)
                2 -> SwarmVisualizerScreen(viewModel = viewModel)
                3 -> AiTrainingFacilityScreen(viewModel = viewModel)
                4 -> ClientDevicePreviewScreen(viewModel = viewModel)
                5 -> MutationLogsScreen(viewModel = viewModel)
                else -> ApkMakerScreen(viewModel = viewModel)
            }
        }
    }
}
