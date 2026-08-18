package com.example.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApiClient {
    private const val TAG = "GeminiApiClient"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    data class InferenceResult(
        val isRealApiCall: Boolean,
        val outputText: String,
        val latencyMs: Long,
        val tokenCountEstimated: Int,
        val isSuccess: Boolean,
        val modelUsed: String,
        val errorMessage: String? = null
    )

    suspend fun generateContent(
        prompt: String,
        apiKey: String?,
        modelId: String = "gemini-2.5-flash",
        systemInstruction: String = "You are Devator, a senior Android engineer and cyber-brutalist software architect. Provide real, concise Kotlin/Compose code."
    ): InferenceResult = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()

        if (apiKey.isNullOrBlank()) {
            // Transparent Local Heuristic Engine when no API key is provided
            val localOutput = generateLocalHeuristicResponse(prompt, modelId)
            val duration = System.currentTimeMillis() - startTime
            return@withContext InferenceResult(
                isRealApiCall = false,
                outputText = localOutput,
                latencyMs = duration,
                tokenCountEstimated = localOutput.split(Regex("\\s+")).size,
                isSuccess = true,
                modelUsed = "$modelId (Local Rule Engine)"
            )
        }

        val cleanKey = apiKey.trim()
        val targetModel = if (modelId.contains("flash-lite", ignoreCase = true)) {
            "gemini-2.5-flash"
        } else {
            "gemini-2.5-flash"
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/$targetModel:generateContent?key=$cleanKey"

        try {
            val jsonPayload = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", "$systemInstruction\n\nTask:\n$prompt")
                            })
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                val genConfig = JSONObject().apply {
                    put("temperature", 0.7)
                    put("maxOutputTokens", 1024)
                }
                put("generationConfig", genConfig)
            }

            val body = jsonPayload.toString().toRequestBody(JSON_MEDIA_TYPE)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val latency = System.currentTimeMillis() - startTime
            val responseBodyString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.w(TAG, "API call failed with code ${response.code}: $responseBodyString")
                val errorMsg = try {
                    val errJson = JSONObject(responseBodyString)
                    errJson.optJSONObject("error")?.optString("message") ?: "HTTP ${response.code}"
                } catch (e: Exception) {
                    "HTTP ${response.code}: $responseBodyString"
                }

                return@withContext InferenceResult(
                    isRealApiCall = true,
                    outputText = "[API ERROR ${response.code}]\n$errorMsg\n\nFalling back to local heuristic response:\n" +
                            generateLocalHeuristicResponse(prompt, modelId),
                    latencyMs = latency,
                    tokenCountEstimated = 0,
                    isSuccess = false,
                    modelUsed = targetModel,
                    errorMessage = errorMsg
                )
            }

            val jsonResponse = JSONObject(responseBodyString)
            val candidates = jsonResponse.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val content = candidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text") ?: "No text generated."

            val usageMetadata = jsonResponse.optJSONObject("usageMetadata")
            val totalTokens = usageMetadata?.optInt("totalTokenCount", text.split(Regex("\\s+")).size)
                ?: text.split(Regex("\\s+")).size

            return@withContext InferenceResult(
                isRealApiCall = true,
                outputText = text,
                latencyMs = latency,
                tokenCountEstimated = totalTokens,
                isSuccess = true,
                modelUsed = targetModel
            )
        } catch (e: Exception) {
            Log.e(TAG, "Network/parsing exception during Gemini API call", e)
            val latency = System.currentTimeMillis() - startTime
            return@withContext InferenceResult(
                isRealApiCall = true,
                outputText = "[NETWORK/CLIENT EXCEPTION]: ${e.localizedMessage ?: e.message}\n\n" +
                        "Local Rule Engine Fallback:\n" + generateLocalHeuristicResponse(prompt, modelId),
                latencyMs = latency,
                tokenCountEstimated = 0,
                isSuccess = false,
                modelUsed = modelId,
                errorMessage = e.message
            )
        }
    }

    private fun generateLocalHeuristicResponse(prompt: String, modelId: String): String {
        val lowerPrompt = prompt.lowercase()
        return when {
            lowerPrompt.contains("compose") || lowerPrompt.contains("ui") || lowerPrompt.contains("button") -> """
                // [LOCAL COMPOSE SYNTHESIZER - MODEL: $modelId]
                @Composable
                fun DevatorDynamicCard(
                    title: String,
                    statusText: String,
                    onActionClick: () -> Unit,
                    modifier: Modifier = Modifier
                ) {
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.primary, CutCornerShape(4.dp)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = title, style = MaterialTheme.typography.titleMedium)
                            Text(text = "STATUS: ${"$"}{statusText.uppercase()}", color = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onActionClick) {
                                Text("EXECUTE ACTION")
                            }
                        }
                    }
                }
            """.trimIndent()

            lowerPrompt.contains("room") || lowerPrompt.contains("database") || lowerPrompt.contains("entity") -> """
                // [LOCAL ROOM SCHEMA GENERATOR - MODEL: $modelId]
                @Entity(tableName = "devator_records")
                data class DevatorRecordEntity(
                    @PrimaryKey(autoGenerate = true) val id: Long = 0,
                    val recordTag: String,
                    val timestamp: Long = System.currentTimeMillis(),
                    val payloadJson: String,
                    val isSynced: Boolean = false
                )

                @Dao
                interface DevatorRecordDao {
                    @Query("SELECT * FROM devator_records ORDER BY timestamp DESC")
                    fun getAllRecords(): Flow<List<DevatorRecordEntity>>

                    @Insert(onConflict = OnConflictStrategy.REPLACE)
                    suspend fun insertRecord(record: DevatorRecordEntity): Long
                }
            """.trimIndent()

            lowerPrompt.contains("gradle") || lowerPrompt.contains("build") || lowerPrompt.contains("manifest") -> """
                // [LOCAL GRADLE / MANIFEST GENERATOR - MODEL: $modelId]
                plugins {
                    alias(libs.plugins.android.application)
                    alias(libs.plugins.kotlin.compose)
                }

                android {
                    namespace = "com.devator.autobuild"
                    compileSdk = 35

                    defaultConfig {
                        applicationId = "com.devator.autobuild"
                        minSdk = 24
                        targetSdk = 35
                        versionCode = 101
                        versionName = "1.0.1"
                    }
                }
            """.trimIndent()

            else -> """
                [LOCAL DEVATOR ARCHITECT ENGINE - MODEL: $modelId]
                - Task: $prompt
                - Status: Processed locally via built-in deterministic heuristic engine.
                - Note: To perform real cloud-based LLM inference, configure your Google Gemini API key in the AI Training Lab.
                - Architectural Recommendation: Use Kotlin Coroutines Flow for reactive updates and Room SQLite for persistence.
            """.trimIndent()
        }
    }
}
