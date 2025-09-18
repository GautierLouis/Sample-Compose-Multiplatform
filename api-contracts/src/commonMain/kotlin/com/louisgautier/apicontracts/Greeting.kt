package com.louisgautier.apicontracts

class Greeting {
    val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}