package com.charlatano.scripts

import com.charlatano.game.hooks.onGround
import com.charlatano.settings.BUNNY_HOP_KEY
import com.charlatano.utils.randBoolean
import com.charlatano.utils.randInt
import com.charlatano.utils.randLong
import org.jire.arrowhead.keyPressed

fun bunnyHop() = onGround {
	if (keyPressed(BUNNY_HOP_KEY)) {
		randScroll()
		Thread.sleep(8 + randLong(10))
		randScroll()
	}
}

private fun randScroll() {
	Thread.sleep(randLong(1, 4))
	val amount = randInt(60) + 10
	robot.mouseWheel(if (randBoolean()) amount else -amount)
}