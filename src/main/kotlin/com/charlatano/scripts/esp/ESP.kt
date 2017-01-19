package com.charlatano.scripts.esp

import com.charlatano.settings.BOX_ESP
import com.charlatano.settings.GLOW_ESP
import com.charlatano.settings.SKELETON_ESP

fun esp() {
	if (GLOW_ESP) glowEsp()
	if (BOX_ESP) boxEsp()
	if (SKELETON_ESP) skeletonEsp()
}