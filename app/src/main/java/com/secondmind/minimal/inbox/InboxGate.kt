package com.secondmind.minimal.inbox
/** Session gate: when true, listener may snapshot; otherwise dormant. */
object InboxGate { @Volatile var active: Boolean = false }
