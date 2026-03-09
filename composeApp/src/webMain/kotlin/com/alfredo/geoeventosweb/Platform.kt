package com.alfredo.geoeventosweb

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform