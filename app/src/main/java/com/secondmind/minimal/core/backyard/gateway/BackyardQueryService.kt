package com.secondmind.minimal.core.backyard.gateway
import com.secondmind.minimal.core.backyard.policy.BackyardAllowlist
import com.secondmind.minimal.core.backyard.storeroom.BackyardRepo
/** Narrow, read-only API the AI may query. */
class BackyardQueryService(private val repo: BackyardRepo = BackyardRepo()) {
  fun get(key: String): String? {
    if (!BackyardAllowlist.isAllowed(key)) return null
    return repo.getFact(key)?.value
  }
  fun list(prefix: String): Map<String,String> =
    repo.list(prefix).filter { BackyardAllowlist.isAllowed(it.key) }.associate { it.key to it.value }
}
