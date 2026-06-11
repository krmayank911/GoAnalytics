package `in`.go.analytics_sdk.internals

class UserManager {

    var userId: String? = null
        private set

    fun identify(userId: String) {
        this.userId = userId
    }
}