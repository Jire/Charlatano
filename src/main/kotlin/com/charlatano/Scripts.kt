package com.charlatano

import com.charlatano.scripts.bunnyHop
import com.charlatano.scripts.esp.esp
import com.charlatano.scripts.fovAim
import com.charlatano.scripts.rcs

/**
 * Invoke the scripts you'd like to use below.
 *
 * If a script is commented (has a "//" before it), it is not enabled.
 */
internal fun scripts() {
	bunnyHop()
	rcs()
	esp()
	fovAim()
	//fovTrigger()
	//noFlash()
	//bombTimer()
}