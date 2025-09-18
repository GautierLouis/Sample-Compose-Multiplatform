package com.louisgautier.apicontracts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform