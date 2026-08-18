package com.example.domain

data class AiModelInfo(
    val id: String,
    val name: String,
    val provider: String,
    val version: String,
    val tier: String,
    val contextWindow: String,
    val specialty: String,
    val status: String,
    val evaluateorScore: Int,
    val speedTokensPerSec: Int,
    val isGeminiRestCapable: Boolean = false,
    val description: String
)

object FreeAiModelCatalog {
    val models = listOf(
        AiModelInfo(
            id = "gemini_35_flash",
            name = "Gemini 3.5 Flash",
            provider = "Google DeepMind",
            version = "3.5-flash",
            tier = "FREE TIER API",
            contextWindow = "1,000,000 Tokens",
            specialty = "Full-Stack Code Synthesis & Rapid Mutator",
            status = "ACTIVE_ENGINE",
            evaluateorScore = 98,
            speedTokensPerSec = 145,
            isGeminiRestCapable = true,
            description = "Google's premier lightweight model with 1M token context. Handles complex coroutine logic and Android bytecode mutations."
        ),
        AiModelInfo(
            id = "gemini_31_flash_lite",
            name = "Gemini 3.1 Flash Lite",
            provider = "Google DeepMind",
            version = "3.1-flash-lite-preview",
            tier = "FREE TIER API",
            contextWindow = "500,000 Tokens",
            specialty = "Sub-Second Micro-Agent Logic & Triage",
            status = "READY",
            evaluateorScore = 95,
            speedTokensPerSec = 220,
            isGeminiRestCapable = true,
            description = "Ultra-fast response model optimized for Swarm coroutine micro-tasks and instant pre-build security screening."
        ),
        AiModelInfo(
            id = "deepseek_r1_distill",
            name = "DeepSeek R1 Distill 70B",
            provider = "DeepSeek AI",
            version = "R1-Distill-Llama-70B",
            tier = "OPEN WEIGHTS / FREE",
            contextWindow = "128,000 Tokens",
            specialty = "Chain-of-Thought Logic Verification & Math",
            status = "READY",
            evaluateorScore = 96,
            speedTokensPerSec = 88,
            isGeminiRestCapable = false,
            description = "Reasoning model that generates detailed step-by-step consensus verification before applying Devator code mutations."
        ),
        AiModelInfo(
            id = "llama_33_70b",
            name = "Llama 3.3 70B Instruct",
            provider = "Meta AI",
            version = "3.3-70b-instruct",
            tier = "FREE INFERENCE TIER",
            contextWindow = "128,000 Tokens",
            specialty = "Architecture Compliance & Refactoring",
            status = "READY",
            evaluateorScore = 94,
            speedTokensPerSec = 110,
            isGeminiRestCapable = false,
            description = "Meta's flagship open model for strictly structured Kotlin class generation and Room schema validation."
        ),
        AiModelInfo(
            id = "qwen_25_coder",
            name = "Qwen 2.5 Coder 32B",
            provider = "Alibaba Cloud",
            version = "2.5-coder-32b",
            tier = "OPEN WEIGHTS / FREE",
            contextWindow = "32,000 Tokens",
            specialty = "Jetpack Compose UI & Kotlin Expert",
            status = "READY",
            evaluateorScore = 97,
            speedTokensPerSec = 130,
            isGeminiRestCapable = false,
            description = "Specialized programming model tuned specifically on Android Jetpack Compose layouts, state flows, and Material 3 design."
        ),
        AiModelInfo(
            id = "mistral_nemo_12b",
            name = "Mistral NeMo 12B",
            provider = "Mistral AI",
            version = "NeMo-12B-Instruct",
            tier = "OPEN ACCESS FREE",
            contextWindow = "128,000 Tokens",
            specialty = "JSON Spec Parsing & Fast Patching",
            status = "READY",
            evaluateorScore = 92,
            speedTokensPerSec = 165,
            isGeminiRestCapable = false,
            description = "Compact open-source model specialized in generating rigid JSON mutation manifests and ProGuard shrinker rules."
        ),
        AiModelInfo(
            id = "phi_4_slm",
            name = "Phi-4 SLM 14B",
            provider = "Microsoft Research",
            version = "Phi-4-Mini",
            tier = "ON-DEVICE SLM FREE",
            contextWindow = "16,000 Tokens",
            specialty = "Local On-Device Swarm Execution",
            status = "LOADED",
            evaluateorScore = 90,
            speedTokensPerSec = 210,
            isGeminiRestCapable = false,
            description = "Small language model optimized for edge deployment directly inside Android APKs for zero-network local decision making."
        )
    )
}
