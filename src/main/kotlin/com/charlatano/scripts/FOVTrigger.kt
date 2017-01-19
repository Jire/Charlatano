package com.charlatano.scripts

import com.charlatano.game.angle
import com.charlatano.game.clientState
import com.charlatano.game.entity.position
import com.charlatano.game.me
import com.charlatano.settings.FIRE_KEY
import com.charlatano.settings.TRIGGER_BONE
import com.charlatano.settings.TRIGGER_FOV
import com.charlatano.utils.hook
import com.charlatano.utils.randLong
import org.jire.arrowhead.keyReleased
import java.awt.event.InputEvent

private val onTriggerTarget = hook(1) {
	findTarget(me.position(), clientState.angle(), false, TRIGGER_FOV, TRIGGER_BONE) >= 0
}

fun fovTrigger() = onTriggerTarget {
	if (keyReleased(FIRE_KEY)) {
		robot.mousePress(InputEvent.BUTTON1_MASK)
		Thread.sleep(8 + randLong(16))
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
	}
}