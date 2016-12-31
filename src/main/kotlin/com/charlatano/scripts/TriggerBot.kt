package com.charlatano.scripts

import co.paralleluniverse.strands.Strand
import com.charlatano.TRIGGER_FOV
import com.charlatano.utils.hook
import com.charlatano.utils.randLong
import org.jire.arrowhead.keyReleased
import java.awt.event.InputEvent

private val onTarget = hook {
	if (target.get() < 0) false
	else {
		val fov = targetFOV.get()
		fov >= 0 && fov <= TRIGGER_FOV
	}
}

fun triggerBot() = onTarget {
	if (keyReleased(1)) {
		robot.mousePress(InputEvent.BUTTON1_MASK)
		Strand.sleep(8 + randLong(16))
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
	}
}