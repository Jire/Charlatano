package com.charlatano.scripts

import com.charlatano.scripts.esp.boxEsp
import com.charlatano.scripts.esp.glowEsp
import com.charlatano.scripts.esp.skeletonEsp
import com.charlatano.settings.BOX_ESP
import com.charlatano.settings.GLOW_ESP
import com.charlatano.settings.SKELETON_ESP

fun esp() {
	if (GLOW_ESP) glowEsp()
	if (BOX_ESP) boxEsp()
	if (SKELETON_ESP) skeletonEsp()
}