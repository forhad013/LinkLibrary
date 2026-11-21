package com.greenrobotdev.lordofthelinks.design.debug

/**
 * Logcat lines prefixed "LoL/<tag>", only in debug builds: the app turns [enabled] on at startup
 * when the build is debuggable, so release builds print nothing. The message is built lazily.
 */
object DebugLog {
    var enabled: Boolean = false

    fun d(tag: String, message: () -> String) {
        if (enabled) println("LoL/$tag ${message()}")
    }
}
