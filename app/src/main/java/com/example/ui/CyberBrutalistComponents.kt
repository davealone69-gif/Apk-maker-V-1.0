package com.example.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberNeonCyan,
    borderWidth: Dp = 1.5.dp,
    title: String? = null,
    badgeText: String? = null,
    badgeColor: Color = CyberNeonLime,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = CutCornerShape(topStart = 10.dp, bottomEnd = 10.dp)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(CyberCardBg)
            .border(borderWidth, borderColor, shape)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .padding(14.dp)
    ) {
        Column {
            if (title != null || badgeText != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (title != null) {
                        Text(
                            text = title.uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = CyberTextPrimary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (badgeText != null) {
                        CyberBadge(text = badgeText, color = badgeColor)
                    }
                }
            }
            content()
        }
    }
}

@Composable
fun CyberBadge(
    text: String,
    color: Color = CyberNeonLime,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(2.dp),
        color = color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color, shape = RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    color: Color = CyberNeonCyan,
    textColor: Color = CyberObsidian,
    enabled: Boolean = true,
    testTagStr: String = "cyber_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .testTag(testTagStr)
            .height(48.dp),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = textColor,
            disabledContainerColor = CyberSurfaceDark,
            disabledContentColor = CyberTextSecondary
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (color == CyberSurfaceDark) CyberBorderLine else color)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text.uppercase(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

/**
 * btn-primary
 * 48dp height, 16dp horizontal padding, 6dp radius, 14sp medium text, primary background
 */
@Composable
fun BtnPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    testTagStr: String = "btn_primary"
) {
    CyberButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        color = CyberNeonCyan,
        textColor = CyberObsidian,
        enabled = enabled,
        testTagStr = testTagStr
    )
}

/**
 * btn-secondary
 * 48dp height, 16dp horizontal padding, 6dp radius, 14sp medium text, secondary surface background
 */
@Composable
fun BtnSecondary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    testTagStr: String = "btn_secondary"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .testTag(testTagStr)
            .height(48.dp),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CyberSurfaceDark,
            contentColor = CyberTextPrimary,
            disabledContainerColor = CyberObsidian,
            disabledContentColor = CyberTextSecondary
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorderLine)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = CyberTextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text.uppercase(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

/**
 * header-btn
 * 40dp height, 12dp horizontal padding, 6dp radius, transparent → surface-2
 */
@Composable
fun HeaderBtn(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isActive: Boolean = false,
    testTagStr: String = "header_btn"
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .testTag(testTagStr)
            .height(40.dp),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) CyberSurfaceDark else Color.Transparent,
            contentColor = if (isActive) CyberNeonCyan else CyberTextSecondary
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isActive) CyberNeonCyan else CyberBorderLine
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (isActive) CyberNeonCyan else CyberTextSecondary
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text.uppercase(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
}

/**
 * icon
 * 48dp × 48dp touch target, 24dp icon, 6dp radius
 */
@Composable
fun CyberIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = CyberNeonCyan,
    backgroundColor: Color = CyberSurfaceDark,
    borderColor: Color = CyberBorderLine,
    testTagStr: String = "cyber_icon_button"
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .testTag(testTagStr),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = tint
        )
    }
}

/**
 * icon-danger
 * 48dp × 48dp touch target, danger background, 24dp icon, 6dp radius
 */
@Composable
fun CyberIconDangerButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    testTagStr: String = "cyber_icon_danger_button"
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(CyberErrorRed)
            .border(1.dp, CyberErrorRed, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .testTag(testTagStr),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = CyberObsidian
        )
    }
}

/**
 * btn-execute
 * 52dp height, full width, 16dp horizontal padding, 6dp radius, 14sp bold, success background
 */
@Composable
fun BtnExecute(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    testTagStr: String = "btn_execute"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .testTag(testTagStr)
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CyberNeonLime,
            contentColor = CyberObsidian,
            disabledContainerColor = CyberSurfaceDark,
            disabledContentColor = CyberTextSecondary
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberNeonLime)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = CyberObsidian
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text.uppercase(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

/**
 * btn-danger
 * 48dp height, 16dp horizontal padding, 6dp radius, danger background, dark text
 */
@Composable
fun BtnDanger(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    testTagStr: String = "btn_danger"
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .testTag(testTagStr)
            .height(48.dp),
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CyberErrorRed,
            contentColor = CyberObsidian,
            disabledContainerColor = CyberSurfaceDark,
            disabledContentColor = CyberTextSecondary
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberErrorRed)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = CyberObsidian
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text.uppercase(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

@Composable
fun GlitchHeader(
    title: String,
    subtitle: String? = null,
    isGlitchActive: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "GlitchAlpha")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Alpha"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        if (isGlitchActive) CyberMagenta else CyberNeonCyan,
                        CutCornerShape(3.dp)
                    )
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.headlineLarge,
                color = if (isGlitchActive) CyberMagenta else CyberTextPrimary,
                modifier = Modifier.alpha(if (isGlitchActive) alphaAnim else 1f)
            )
        }
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = CyberNeonLime,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(start = 22.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun CyberMetricBar(
    label: String,
    score: Int,
    color: Color = CyberNeonCyan,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = CyberTextSecondary
            )
            Text(
                text = "$score / 100",
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = color,
            trackColor = CyberSurfaceDark
        )
    }
}

@Composable
fun TerminalBox(
    logs: List<String>,
    modifier: Modifier = Modifier,
    title: String = "DEVATOR SWARM TERMINAL FEED"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
            .background(CyberObsidian)
            .border(1.dp, CyberBorderLine, CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(CyberTerminalGreen, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium,
                        color = CyberTerminalGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
                CyberBadge(text = "LIVE", color = CyberNeonCyan)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(CyberSurfaceDark)
                    .padding(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (logs.isEmpty()) {
                        Text(
                            text = "> System ready. Awaiting mutation dispatch...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CyberTextSecondary,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        logs.takeLast(6).forEach { logLine ->
                            Text(
                                text = "> $logLine",
                                style = MaterialTheme.typography.bodyMedium,
                                color = when {
                                    logLine.contains("APPROVED") || logLine.contains("PASSED") -> CyberNeonLime
                                    logLine.contains("REJECTED") || logLine.contains("FAILED") -> CyberErrorRed
                                    logLine.contains("MUTATING") || logLine.contains("Drafting") -> CyberYellow
                                    else -> CyberNeonCyan
                                },
                                fontFamily = FontFamily.Monospace,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
