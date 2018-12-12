package com.charlatano.scripts


import com.charlatano.game.*
import com.charlatano.scripts.*
import com.charlatano.settings.*
import com.charlatano.utils.*
import org.jire.arrowhead.keyPressed
import java.awt.event.KeyEvent
import org.jire.arrowhead.keyPressed


fun toggleESP() = every(10) {
    if (keyPressed(0x12) && keyPressed(KeyEvent.VK_0)) {
        ENABLE_ESP = !ENABLE_ESP
      do { 
        Thread.sleep(25) 
      } while (keyPressed(KeyEvent.VK_0))
    }
}
