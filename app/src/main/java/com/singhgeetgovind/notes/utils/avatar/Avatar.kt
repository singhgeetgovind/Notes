package com.singhgeetgovind.notes.utils.avatar

interface Avatar{
    val URL: String get() = "https://api.dicebear.com/9.x/"

    fun getBASEURL():String
    fun <T : Enum<T>> iterator(values: () -> Array<T>): T = values()
        .random()
}