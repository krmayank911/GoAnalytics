package `in`.go.analytics_sdk.internals.configs

class SystemClock: GoClock {
    override fun now(): Long {
        return System.currentTimeMillis()
    }
}