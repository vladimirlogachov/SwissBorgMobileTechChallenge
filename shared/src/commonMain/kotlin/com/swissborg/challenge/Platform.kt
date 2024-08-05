package com.swissborg.challenge

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform