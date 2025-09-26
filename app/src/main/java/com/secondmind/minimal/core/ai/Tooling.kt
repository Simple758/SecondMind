package com.secondmind.minimal.core.ai

import org.json.JSONObject

/** Minimal tool API for future self-rewriting assistant. */
interface Tool {
    val name: String
    suspend fun invoke(args: JSONObject): JSONObject
}

class ToolRouter(private val tools: Map<String, Tool> = emptyMap()) {
    fun has(name: String) = tools.containsKey(name)
    suspend fun call(name: String, args: JSONObject): JSONObject =
        tools[name]?.invoke(args) ?: JSONObject().put("error", "tool_not_found")
}

/** Sketch for code-patching; left as a stub for future evolution. */
class ToolPatchCode : Tool {
    override val name: String = "patch_code"
    override suspend fun invoke(args: JSONObject): JSONObject {
        // TODO: apply code patches; return summary
        return JSONObject().put("ok", true)
    }
}
