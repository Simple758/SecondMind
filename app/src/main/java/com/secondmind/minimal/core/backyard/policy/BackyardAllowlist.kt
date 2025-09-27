package com.secondmind.minimal.core.backyard.policy
/** Keys the AI gateway is allowed to expose. Edit explicitly. */
object BackyardAllowlist {
  val keys: Set<String> = setOf(
    "notif.count.1h",
    "usage.screen.on.ms",
    "network.tx.rx.kb"
  )
  fun isAllowed(key: String) = keys.contains(key)
}
