package `in`.go.analytics_sdk.internals.identity

internal class UserManager {

    var userId: String? = null
        private set

    fun identify(userId: String) {
        this.userId = userId
    }
}