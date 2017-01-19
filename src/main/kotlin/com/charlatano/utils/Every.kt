package com.charlatano.utils

import kotlin.concurrent.thread

@Volatile var paused = false

inline fun every(duration: Int, continuous: Boolean = false, crossinline body: () -> Unit) = thread {
	while (!Thread.interrupted()) {
		if (continuous || !paused) body()
		Thread.sleep(duration.toLong())
	}
}