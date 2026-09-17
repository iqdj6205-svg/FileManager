package com.sere.filemanager.core.remote

class RemoteRateLimiter(
    private val maxRequests: Int = 120,
    private val windowMillis: Long = 60_000L,
) {
    private val hits = mutableMapOf<String, ArrayDeque<Long>>()

    fun allow(clientKey: String, now: Long = System.currentTimeMillis()): Boolean = synchronized(hits) {
        val queue = hits.getOrPut(clientKey) { ArrayDeque() }
        while (queue.isNotEmpty() && now - queue.first() > windowMillis) queue.removeFirst()
        if (queue.size >= maxRequests) false else { queue.addLast(now); true }
    }
}
