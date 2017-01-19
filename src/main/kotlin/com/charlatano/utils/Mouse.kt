package com.charlatano.utils

import com.charlatano.utils.natives.CUser32

private const val MOUSEEVENTF_MOVE = 0x0001

fun mouseMove(dx: Int, dy: Int) = CUser32.mouse_event(MOUSEEVENTF_MOVE, dx, dy, 0, 0)