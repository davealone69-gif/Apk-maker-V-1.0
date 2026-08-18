package com.example.mandelacore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MandelaUIRealityState(
    val isGlitchActive: Boolean = false,
    val isShiftInProgress: Boolean = false,
    val activePatchNotification: String? = null,
    val activeClientVersion: String = "1.0.0",
    val statusBadge: String = "REALITY_STABLE"
)

object MandelaCore {

    private val _realityState = MutableStateFlow(MandelaUIRealityState())
    val realityState: StateFlow<MandelaUIRealityState> = _realityState.asStateFlow()

    fun triggerRealityGlitch(durationMs: Long = 800) {
        _realityState.value = _realityState.value.copy(
            isGlitchActive = true,
            statusBadge = "MUTATING_REALITY"
        )
    }

    fun endRealityGlitch() {
        _realityState.value = _realityState.value.copy(
            isGlitchActive = false,
            statusBadge = "REALITY_STABLE"
        )
    }

    fun notifyClientAppUpdate(versionName: String, patchTitle: String) {
        _realityState.value = _realityState.value.copy(
            isShiftInProgress = true,
            activeClientVersion = versionName,
            activePatchNotification = "OTA PATCH $versionName APPLIED: $patchTitle",
            statusBadge = "LIVE_OTA_UPDATED"
        )
    }

    fun clearNotification() {
        _realityState.value = _realityState.value.copy(
            isShiftInProgress = false,
            activePatchNotification = null
        )
    }
}
