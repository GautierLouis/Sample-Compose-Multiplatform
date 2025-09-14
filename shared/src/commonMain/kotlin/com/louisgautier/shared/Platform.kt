package com.louisgautier.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform