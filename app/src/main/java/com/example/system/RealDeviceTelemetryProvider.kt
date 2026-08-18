package com.example.system

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

data class DeviceHardwareReport(
    val manufacturer: String,
    val model: String,
    val brand: String,
    val deviceCode: String,
    val hardwareChipset: String,
    val board: String,
    val supportedAbis: List<String>,
    val androidVersion: String,
    val apiLevel: Int,
    val securityPatch: String,
    val totalRamMb: Long,
    val availRamMb: Long,
    val isLowMemory: Boolean,
    val totalStorageGb: Double,
    val freeStorageGb: Double,
    val batteryPct: Int,
    val isBatteryCharging: Boolean,
    val batteryTemperatureC: Double,
    val batteryVoltageMv: Int,
    val isNetworkConnected: Boolean,
    val networkType: String,
    val linkDownstreamKbps: Int,
    val displayDensityDpi: Int,
    val screenResolution: String
)

data class NetworkPingResult(
    val url: String,
    val isSuccess: Boolean,
    val statusCode: Int,
    val latencyMs: Long,
    val protocol: String,
    val message: String
)

data class CryptoHashResult(
    val algorithm: String,
    val inputBytesLength: Int,
    val hexHash: String
)

object RealDeviceTelemetryProvider {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    fun inspectHardware(context: Context): DeviceHardwareReport {
        // RAM Metrics via ActivityManager
        val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager?.getMemoryInfo(memInfo)
        val totalRamMb = memInfo.totalMem / (1024 * 1024)
        val availRamMb = memInfo.availMem / (1024 * 1024)
        val isLowMem = memInfo.lowMemory

        // Storage Metrics via StatFs
        val statFs = try {
            StatFs(Environment.getDataDirectory().path)
        } catch (e: Exception) {
            null
        }
        val blockSize = statFs?.blockSizeLong ?: 1024L
        val totalBlocks = statFs?.blockCountLong ?: 0L
        val freeBlocks = statFs?.availableBlocksLong ?: 0L
        val totalStorageGb = (totalBlocks * blockSize).toDouble() / (1024.0 * 1024.0 * 1024.0)
        val freeStorageGb = (freeBlocks * blockSize).toDouble() / (1024.0 * 1024.0 * 1024.0)

        // Battery Metrics via sticky Intent
        val batteryIntent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: 100
        val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: 100
        val batteryPct = if (level >= 0 && scale > 0) ((level.toFloat() / scale.toFloat()) * 100).toInt() else 100
        val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
        val rawTemp = batteryIntent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        val batteryTempC = rawTemp / 10.0
        val voltageMv = batteryIntent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) ?: 0

        // Network Metrics via ConnectivityManager
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetwork = connManager?.activeNetwork
        val caps = connManager?.getNetworkCapabilities(activeNetwork)
        val isConnected = caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val netType = when {
            caps == null -> "NONE"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WI-FI"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "CELLULAR"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ETHERNET"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
            else -> "ACTIVE"
        }
        val downstreamKbps = caps?.linkDownstreamBandwidthKbps ?: 0

        // Screen & Display
        val dm = context.resources.displayMetrics
        val densityDpi = dm.densityDpi
        val res = "${dm.widthPixels}x${dm.heightPixels}"

        return DeviceHardwareReport(
            manufacturer = Build.MANUFACTURER.replaceFirstChar { it.uppercase() },
            model = Build.MODEL,
            brand = Build.BRAND.uppercase(),
            deviceCode = Build.DEVICE,
            hardwareChipset = Build.HARDWARE,
            board = Build.BOARD,
            supportedAbis = Build.SUPPORTED_ABIS.toList(),
            androidVersion = Build.VERSION.RELEASE,
            apiLevel = Build.VERSION.SDK_INT,
            securityPatch = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Build.VERSION.SECURITY_PATCH else "N/A",
            totalRamMb = totalRamMb,
            availRamMb = availRamMb,
            isLowMemory = isLowMem,
            totalStorageGb = String.format("%.2f", totalStorageGb).toDoubleOrNull() ?: totalStorageGb,
            freeStorageGb = String.format("%.2f", freeStorageGb).toDoubleOrNull() ?: freeStorageGb,
            batteryPct = batteryPct,
            isBatteryCharging = isCharging,
            batteryTemperatureC = batteryTempC,
            batteryVoltageMv = voltageMv,
            isNetworkConnected = isConnected,
            networkType = netType,
            linkDownstreamKbps = downstreamKbps,
            displayDensityDpi = densityDpi,
            screenResolution = res
        )
    }

    suspend fun executeHttpPing(targetUrl: String = "https://www.google.com/generate_204"): NetworkPingResult =
        withContext(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            try {
                val request = Request.Builder()
                    .url(targetUrl)
                    .get()
                    .build()
                val response = httpClient.newCall(request).execute()
                val latency = System.currentTimeMillis() - startTime
                val code = response.code
                val protocolStr = response.protocol.toString()
                response.close()
                NetworkPingResult(
                    url = targetUrl,
                    isSuccess = response.isSuccessful,
                    statusCode = code,
                    latencyMs = latency,
                    protocol = protocolStr,
                    message = "HTTP $code - OK (${latency}ms round-trip)"
                )
            } catch (e: Exception) {
                val latency = System.currentTimeMillis() - startTime
                NetworkPingResult(
                    url = targetUrl,
                    isSuccess = false,
                    statusCode = 0,
                    latencyMs = latency,
                    protocol = "NONE",
                    message = "Ping failed: ${e.localizedMessage ?: e.message}"
                )
            }
        }

    fun computeChecksums(data: String): List<CryptoHashResult> {
        val bytes = data.toByteArray(Charsets.UTF_8)
        val algos = listOf("MD5", "SHA-1", "SHA-256", "SHA-512")
        return algos.map { algo ->
            val md = MessageDigest.getInstance(algo)
            val digest = md.digest(bytes)
            val hex = digest.joinToString("") { "%02x".format(it) }
            CryptoHashResult(
                algorithm = algo,
                inputBytesLength = bytes.size,
                hexHash = hex
            )
        }
    }
}
