package com.charlatano.utils

inline fun <R> retry(duration: Long = 1000, noinline exceptionHandler: ((Throwable) -> Unit)? = null, body: () -> R) {
	while (!Thread.interrupted()) {
		try {
			body()
			break
		} catch (t: Throwable) {
			exceptionHandler?.invoke(t)
			Thread.sleep(duration)
		}
	}
}