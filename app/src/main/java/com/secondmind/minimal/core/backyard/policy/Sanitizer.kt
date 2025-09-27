package com.secondmind.minimal.core.backyard.policy
/** Minimal sanitizers: clamp precision, strip obvious PII. */
object Sanitizer {
  private val email = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
  private val phone = Regex("\\b\\+?\\d{7,}\\b")
  fun clamp(s: String, max: Int = 240) = s.take(max)
  fun redact(s: String) = s.replace(email, "###").replace(phone, "###")
}
