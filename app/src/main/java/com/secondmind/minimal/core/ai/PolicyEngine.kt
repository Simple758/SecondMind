package com.secondmind.minimal.core.ai

object PolicyEngine {
    private val email = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    private val phone = Regex("\\b\\+?[0-9][0-9\\-() ]{6,}[0-9]\\b")
    private val cc = Regex("\\b(?:\\d[ -]*?){13,19}\\b")

    fun redact(input: String?): String? {
        if (input.isNullOrEmpty()) return input
        var out = input
        out = email.replace(out, "[email]")
        out = phone.replace(out, "[phone]")
        out = cc.replace(out, "[card]")
        return out
    }

    fun apply(packet: ContextPacket): ContextPacket =
        packet.copy(
            text = redact(packet.text),
            extras = packet.extras.mapValues { redact(it.value) ?: "" }
        )
}
