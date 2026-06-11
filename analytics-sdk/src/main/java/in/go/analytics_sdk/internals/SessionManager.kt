package `in`.go.analytics_sdk.internals

import java.util.UUID

internal class SessionManager {
    val sessionId = UUID.randomUUID().toString()
}