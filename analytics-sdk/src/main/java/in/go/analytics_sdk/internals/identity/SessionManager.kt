package `in`.go.analytics_sdk.internals.identity

import java.util.UUID

internal class SessionManager {
    val sessionId = UUID.randomUUID().toString()
}