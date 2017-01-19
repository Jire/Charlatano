package com.charlatano.utils.extensions

internal fun ByteArray.toNetVarString(): String {
	for (i in 0..size - 1) if (0.toByte() == this[i]) this[i] = 32
	return String(this).split(" ")[0].trim()
}