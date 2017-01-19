package com.charlatano.utils

data class RepeatedInt(val value: Int, val repeats: Int)

operator fun Int.get(repeats: Int) = RepeatedInt(this, repeats)