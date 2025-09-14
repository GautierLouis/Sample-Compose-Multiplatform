package com.louisgautier.shared

class Greeting {
    val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}